package com.huzhi.app.controller.waybill;

import com.alibaba.fastjson.JSONObject;
import com.huzhi.app.domain.car.WrapperPageVO;
import com.huzhi.app.domain.driver.DriverWpVO;
import com.huzhi.app.domain.waybill.WaybillInfoVO;
import com.huzhi.app.domain.waybill.WaybillListBaseVO;
import com.huzhi.app.domain.waybill.WaybillListVO;
import com.huzhi.app.domain.waybill.WaybillWpVO;
import com.huzhi.app.domain.waybillSnapshot.WaybillSnapshotListBaseVO;
import com.huzhi.app.domain.waybillSnapshot.WaybillSnapshotListVO;
import com.huzhi.app.domain.waybillSnapshot.WaybillSnapshotVO;
import com.huzhi.module.module.car.entity.Car;
import com.huzhi.module.module.car.service.CarService;
import com.huzhi.module.module.client.entity.Client;
import com.huzhi.module.module.client.service.ClientService;
import com.huzhi.module.module.company.entity.Company;
import com.huzhi.module.module.company.service.CompanyService;
import com.huzhi.module.module.driver.entity.Driver;
import com.huzhi.module.module.driver.service.DriverService;
import com.huzhi.module.module.user.entity.User;
import com.huzhi.module.module.waybill.entity.Waybill;
import com.huzhi.module.module.waybill.service.BaseWaybillRelevanceService;
import com.huzhi.module.module.waybill.service.WaybillService;
import com.huzhi.module.module.waybillSnapshot.entity.WaybillSnapshot;
import com.huzhi.module.module.waybillSnapshot.service.WaybillSnapshotService;
import com.huzhi.module.response.Response;
import com.huzhi.module.utils.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class WaybillController {
    private static final int pageSize =5;
    private final WaybillService waybillService;
    private final BaseWaybillRelevanceService baseWaybillService;
    private final CompanyService companyService;
    private final ClientService clientService;
    private final DriverService driverService;
    private final CarService carService;
    private final WaybillSnapshotService waybillSnapshotService;

    @Autowired
    public WaybillController(WaybillService waybillService,
                             BaseWaybillRelevanceService baseWaybillService,
                             CompanyService companyService,
                             ClientService clientService,
                             DriverService driverService,
                             CarService carService,
                             WaybillSnapshotService waybillSnapshotService) {
        this.waybillService = waybillService;
        this.baseWaybillService = baseWaybillService;
        this.companyService = companyService;
        this.clientService = clientService;
        this.driverService = driverService;
        this.carService = carService;
        this.waybillSnapshotService=waybillSnapshotService;
    }

    /**
     * 待审核运单列表展示
     */
    @RequestMapping("/waybill/list")
    public Response WaybillList(@RequestParam(value = "wp",required = false) String wp,
                                @RequestParam(value = "driverName", required = false) String driverName,
                                @RequestParam(value = "carNumberPlate", required = false) String carNumberPlate) {
        WaybillWpVO wpMassage=new WaybillWpVO();
        if (BaseUtil.isEmpty(wp)){
            driverName = BaseUtil.isEmpty(driverName)?null:driverName.trim();
            carNumberPlate = BaseUtil.isEmpty(carNumberPlate)?null:carNumberPlate.trim();
            wpMassage.setPage(1);
            wpMassage.setCarNumberPlate(carNumberPlate);
            wpMassage.setDriverName(driverName);
        }else {
            wpMassage= JSONObject.parseObject(Base64.getDecoder().decode(wp.getBytes(StandardCharsets.UTF_8)), WrapperPageVO.class);
            wpMassage.setPage(wpMassage.getPage()+1);
        }
        String wpBase= Base64.getUrlEncoder().encodeToString(JSONObject.toJSONString(wpMassage).getBytes(StandardCharsets.UTF_8));
        try {
            List<Waybill> waybillList = baseWaybillService.getWaybillList(wpMassage.getPage(), pageSize, null, null, driverName, carNumberPlate);
            List<BigInteger> companyIdList = waybillList.stream().map(Waybill::getCompanyId).collect(Collectors.toList());
            List<BigInteger> clientIdList = waybillList.stream().map(Waybill::getClientId).collect(Collectors.toList());
            List<BigInteger> driverIdList = waybillList.stream().map(Waybill::getDriverId).collect(Collectors.toList());
            List<BigInteger> carIdList = waybillList.stream().map(Waybill::getCarId).collect(Collectors.toList());
            List<Company> companies = companyService.getListByIdList(companyIdList);
            List<Client> clients = clientService.getListByIdList(clientIdList);
            List<Driver> drivers = driverService.getListByIdList(driverIdList);
            List<Car> cars = carService.getListByIdList(carIdList);
            Map<BigInteger, String> clientNames = clients.stream().collect(Collectors.toMap(Client::getId, Client::getName));
            Map<BigInteger, String> clientPhones = clients.stream().collect(Collectors.toMap(Client::getId, Client::getPhone));
            Map<BigInteger, String> companyNames = companies.stream().collect(Collectors.toMap(Company::getId, Company::getName));
            Map<BigInteger, String> driverNames;
            driverNames = drivers.stream().collect(Collectors.toMap(Driver::getId, Driver::getName));
            Map<BigInteger, String> carNumberPlates = cars.stream().collect(Collectors.toMap(Car::getId, Car::getNumberPlate));
            List<WaybillListBaseVO> list = new ArrayList<>();
            WaybillListVO waybillListVO = new WaybillListVO();
            for (Waybill w : waybillList) {
                WaybillListBaseVO entity = new WaybillListBaseVO();
                entity.setId(w.getId());
                entity.setWaybillNumber(w.getWaybillNumber());
                entity.setExamineStatus(w.getExamineStatus());
                entity.setClientName(clientNames.get(w.getClientId()));
                entity.setClientPhone(clientPhones.get(w.getClientId()));
                entity.setCompanyName(companyNames.get(w.getCompanyId()));
                entity.setDriverName(driverNames.get(w.getDriverId()));
                entity.setCarNumberPlate(carNumberPlates.get(w.getCarId()));
                entity.setDeliveryTime(BaseUtil.timeStamp2Date(w.getDeliveryTime()));
                entity.setArrivalTime(BaseUtil.timeStamp2Date(w.getArrivalTime()));
                list.add(entity);
            }
            waybillListVO.setWp(wpBase);
            waybillListVO.setIsEnd(waybillList.size() < pageSize);
            waybillListVO.setList(list);
            return new Response(1001, waybillListVO);
        }catch (RuntimeException e){
            log.info("Error WaybillList:"+e.getMessage());
            return new Response(2005);
        }
    }

    /**
     * 待审核运单详情
     */
    @RequestMapping("/waybill/info")
    public Response WaybillInfo(@RequestParam(value = "id") BigInteger id) {
        Waybill waybill = waybillService.getById(id);
        WaybillInfoVO waybillInfoVO = new WaybillInfoVO();
        Client client = clientService.getById(waybill.getClientId());
        Company company = companyService.getById(waybill.getCompanyId());
        Driver driver = driverService.getById(waybill.getDriverId());
        Car car = carService.getById(waybill.getCarId());
        waybillInfoVO.setWaybillNumber(waybill.getWaybillNumber());
        waybillInfoVO.setExamineStatus(waybill.getExamineStatus());
        waybillInfoVO.setExamineRemarks(waybill.getExamineRemarks());
        waybillInfoVO.setTypeOfGoods(waybill.getTypeOfGoods());
        waybillInfoVO.setWeightOfGoods(waybill.getWeightOfGoods());
        waybillInfoVO.setVolumeOfGoods(waybill.getVolumeOfGoods());
        waybillInfoVO.setDeliveryTime(waybill.getDeliveryTime());
        waybillInfoVO.setDeliveryProvince(waybill.getDeliveryProvince());
        waybillInfoVO.setDeliveryCity(waybill.getDeliveryCity());
        waybillInfoVO.setDeliveryRegion(waybill.getDeliveryRegion());
        waybillInfoVO.setDeliveryAddress(waybill.getDeliveryAddress());
        waybillInfoVO.setArrivalTime(waybill.getArrivalTime());
        waybillInfoVO.setArrivalProvince(waybill.getArrivalProvince());
        waybillInfoVO.setArrivalCity(waybill.getArrivalCity());
        waybillInfoVO.setArrivalRegion(waybill.getArrivalRegion());
        waybillInfoVO.setArrivalAddress(waybill.getArrivalAddress());
        waybillInfoVO.setRemarks(waybill.getRemarks());
        waybillInfoVO.setClientName(client.getName());
        waybillInfoVO.setClientPhone(client.getPhone());
        waybillInfoVO.setCompanyName(company.getName());
        waybillInfoVO.setCompanyPhone(company.getPhone());
        waybillInfoVO.setDriverName(driver.getName());
        waybillInfoVO.setDriverPhone(driver.getPhone());
        waybillInfoVO.setCarNumberPlate(car.getNumberPlate());
        return new Response(1001, waybillInfoVO);
    }


    /**
     * 已审核运单列表
     */
    @RequestMapping("/waybill/snapshot/list")
    public Response WaybillSnapshotList(@RequestParam(value = "wp",required = false)String wp,
                                        @RequestParam(value = "driverName", required = false) String driverName,
                                        @RequestParam(value = "carNumberPlate", required = false) String carNumberPlate){
        WaybillWpVO wpMassage=new WaybillWpVO();
        if (BaseUtil.isEmpty(wp)){
            driverName=BaseUtil.isEmpty(driverName)?null:driverName.trim();
            carNumberPlate=BaseUtil.isEmpty(carNumberPlate)?null:carNumberPlate.trim();
            wpMassage.setPage(1);
            wpMassage.setDriverName(driverName);
            wpMassage.setCarNumberPlate(carNumberPlate);
        }else {
            wpMassage= JSONObject.parseObject(Base64.getDecoder().decode(wp.getBytes(StandardCharsets.UTF_8)), WrapperPageVO.class);
            wpMassage.setPage(wpMassage.getPage()+1);
        }
        String wpBase= Base64.getUrlEncoder().encodeToString(JSONObject.toJSONString(wpMassage).getBytes(StandardCharsets.UTF_8));

        try {
            List<WaybillSnapshot> list = waybillSnapshotService.getList(null, null, driverName, carNumberPlate, wpMassage.getPage(), pageSize);
            List<WaybillSnapshotListBaseVO> listVo = new ArrayList<>();
            for (WaybillSnapshot w : list) {
                WaybillSnapshotListBaseVO entity = new WaybillSnapshotListBaseVO();
                entity.setId(w.getId());
                entity.setWaybillNumber(w.getWaybillNumber());
                entity.setClientName(w.getClientName());
                entity.setClientPhone(w.getClientPhone());
                entity.setDriverName(w.getDriverName());
                entity.setCarNumberPlate(w.getCarNumberPlate());
                entity.setDeliveryTime(BaseUtil.timeStamp2Date(w.getDeliveryTime()));
                listVo.add(entity);
            }
            WaybillSnapshotListVO waybillSnapshotListVO = new WaybillSnapshotListVO();
            waybillSnapshotListVO.setWp(wpBase);
            waybillSnapshotListVO.setIsEnd(list.size() < pageSize);
            waybillSnapshotListVO.setList(listVo);
            return new Response(1001, waybillSnapshotListVO);
        }catch (RuntimeException e){
            log.info("Error WaybillSnapshotList:"+e.getMessage());
            return new Response(2005);
        }
    }
    /**
     * 已审核运单详情
     */
    @RequestMapping("/waybill/snapshot/info")
    public Response WaybillSnapshotInfo(@RequestParam(value = "id")BigInteger id){
        WaybillSnapshot waybillSnapshot = waybillSnapshotService.getById(id);
        WaybillSnapshotVO waybillSnapshotVO = new WaybillSnapshotVO();
        waybillSnapshotVO.setWaybillNumber(waybillSnapshot.getWaybillNumber());
        waybillSnapshotVO.setTypeOfGoods(waybillSnapshot.getTypeOfGoods());
        waybillSnapshotVO.setWeightOfGoods(waybillSnapshot.getWeightOfGoods());
        waybillSnapshotVO.setVolumeOfGoods(waybillSnapshot.getVolumeOfGoods());
        waybillSnapshotVO.setDeliveryTime(BaseUtil.timeStamp2Date(waybillSnapshot.getDeliveryTime()));
        waybillSnapshotVO.setDeliveryProvince(waybillSnapshot.getDeliveryProvince());
        waybillSnapshotVO.setDeliveryCity(waybillSnapshot.getDeliveryCity());
        waybillSnapshotVO.setDeliveryRegion(waybillSnapshot.getDeliveryRegion());
        waybillSnapshotVO.setDeliveryAddress(waybillSnapshot.getDeliveryAddress());
        waybillSnapshotVO.setArrivalTime(BaseUtil.timeStamp2Date(waybillSnapshot.getArrivalTime()));
        waybillSnapshotVO.setArrivalProvince(waybillSnapshot.getArrivalProvince());
        waybillSnapshotVO.setArrivalCity(waybillSnapshot.getArrivalCity());
        waybillSnapshotVO.setArrivalRegion(waybillSnapshot.getArrivalRegion());
        waybillSnapshotVO.setArrivalAddress(waybillSnapshot.getArrivalAddress());
        waybillSnapshotVO.setRemarks(waybillSnapshot.getRemarks());
        waybillSnapshotVO.setClientName(waybillSnapshot.getClientName());
        waybillSnapshotVO.setClientPhone(waybillSnapshot.getClientPhone());
        waybillSnapshotVO.setClientCreditCode(waybillSnapshot.getClientCreditCode());
        waybillSnapshotVO.setCompanyName(waybillSnapshot.getCompanyName());
        waybillSnapshotVO.setCompanyPhone(waybillSnapshot.getCompanyPhone());
        waybillSnapshotVO.setCompanyCreditCode(waybillSnapshot.getCompanyCreditCode());
        waybillSnapshotVO.setDriverName(waybillSnapshot.getDriverName());
        waybillSnapshotVO.setDriverPhone(waybillSnapshot.getDriverPhone());
        waybillSnapshotVO.setDriverIdNumber(waybillSnapshot.getDriverIdNumber());
        waybillSnapshotVO.setCarNumberPlate(waybillSnapshot.getCarNumberPlate());
        waybillSnapshotVO.setCarModel(waybillSnapshot.getCarModel());
        return new Response(1001,waybillSnapshotVO);
    }

}
