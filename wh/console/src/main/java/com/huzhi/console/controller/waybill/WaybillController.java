package com.huzhi.console.controller.waybill;

import com.huzhi.console.annotations.VerifiedUser;
import com.huzhi.console.domain.waybill.*;
import com.huzhi.console.domain.waybillSnapshot.WaybillSnapshotListBaseVO;
import com.huzhi.console.domain.waybillSnapshot.WaybillSnapshotListVO;
import com.huzhi.console.domain.waybillSnapshot.WaybillSnapshotVO;
import com.huzhi.module.module.waybill.service.BaseWaybillRelevanceService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class WaybillController {
    private final  Integer pageSize = 5;
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
     * 新增运单
     *
     * @param loginUser        登录检测
     * @param typeOfGoods      货物类型
     * @param weightOfGoods    货物重量
     * @param volumeOfGoods    货物体积
     * @param deliveryTime     运单开始时间
     * @param deliveryProvince 发货省
     * @param deliveryCity     发货市
     * @param deliveryRegion   发货区
     * @param deliveryAddress  发货地址
     * @param arrivalTime      运单结束时间
     * @param arrivalProvince  收货省
     * @param arrivalCity      收货市
     * @param arrivalRegion    收货区
     * @param arrivalAddress   收货地址
     * @param remarks          备注
     * @param clientId         托运人(客户)id
     * @param companyId        承运公司id
     * @param driverId         司机id
     * @param carId            车辆id
     * @return 新增的运单id
     */
    @RequestMapping("/waybill/insert")
    public Response WaybillInsert(@VerifiedUser User loginUser,
                                  @RequestParam(value = "typeOfGoods") String typeOfGoods,
                                  @RequestParam(value = "weightOfGoods") Integer weightOfGoods,
                                  @RequestParam(value = "volumeOfGoods") Integer volumeOfGoods,
                                  @RequestParam(value = "deliveryTime") Integer deliveryTime,
                                  @RequestParam(value = "deliveryProvince") String deliveryProvince,
                                  @RequestParam(value = "deliveryCity") String deliveryCity,
                                  @RequestParam(value = "deliveryRegion") String deliveryRegion,
                                  @RequestParam(value = "deliveryAddress") String deliveryAddress,
                                  @RequestParam(value = "arrivalTime") Integer arrivalTime,
                                  @RequestParam(value = "arrivalProvince") String arrivalProvince,
                                  @RequestParam(value = "arrivalCity") String arrivalCity,
                                  @RequestParam(value = "arrivalRegion") String arrivalRegion,
                                  @RequestParam(value = "arrivalAddress") String arrivalAddress,
                                  @RequestParam(value = "remarks", required = false) String remarks,
                                  @RequestParam(value = "clientId") BigInteger clientId,
                                  @RequestParam(value = "companyId") BigInteger companyId,
                                  @RequestParam(value = "driverId") BigInteger driverId,
                                  @RequestParam(value = "carId") BigInteger carId
    ) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        deliveryAddress = deliveryAddress.trim();
        arrivalAddress = arrivalAddress.trim();
        remarks =BaseUtil.isEmpty(remarks)?null:remarks.trim();
        String message;
        try {
            baseWaybillService.waybillEdit(null,carId, driverId, clientId, companyId, typeOfGoods, weightOfGoods,
                    volumeOfGoods, deliveryTime, deliveryProvince, deliveryCity, deliveryRegion, deliveryAddress,
                    arrivalTime, arrivalProvince, arrivalCity, arrivalRegion, arrivalAddress,
                    remarks,null,null,loginUser.getId(),BaseUtil.currentSeconds(),0);
            return new Response(1001);
        } catch (RuntimeException e) {
            log.info("Error WaybillInsert:"+e.getMessage());
            return new Response(2002);
        }
    }

    /**
     * 删除待审核运单
     */
    @RequestMapping("/waybill/delete")
    public Response WaybillDelete(@VerifiedUser User loginUser,
                                  @RequestParam(value = "id") BigInteger id) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        try{
            waybillService.delete(id);
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error WaybillDelete:"+e.getMessage());
            return new Response(2004);
        }
    }

    /**
     * 修改待审核运单
     */
    @RequestMapping("/waybill/update")
    public Response WaybillUpdate(@VerifiedUser User loginUser,
                                  @RequestParam(value = "id") BigInteger id,
                                  @RequestParam(value = "examineStatus") Integer examineStatus,
                                  @RequestParam(value = "examineRemarks") String examineRemarks,
                                  @RequestParam(value = "typeOfGoods") String typeOfGoods,
                                  @RequestParam(value = "weightOfGoods") Integer weightOfGoods,
                                  @RequestParam(value = "volumeOfGoods") Integer volumeOfGoods,
                                  @RequestParam(value = "deliveryTime") Integer deliveryTime,
                                  @RequestParam(value = "deliveryProvince") String deliveryProvince,
                                  @RequestParam(value = "deliveryCity") String deliveryCity,
                                  @RequestParam(value = "deliveryRegion") String deliveryRegion,
                                  @RequestParam(value = "deliveryAddress") String deliveryAddress,
                                  @RequestParam(value = "arrivalTime") Integer arrivalTime,
                                  @RequestParam(value = "arrivalProvince") String arrivalProvince,
                                  @RequestParam(value = "arrivalCity") String arrivalCity,
                                  @RequestParam(value = "arrivalRegion") String arrivalRegion,
                                  @RequestParam(value = "arrivalAddress") String arrivalAddress,
                                  @RequestParam(value = "remarks", required = false) String remarks,
                                  @RequestParam(value = "connectionEnterpriseId") BigInteger clientId,
                                  @RequestParam(value = "carrierCompanyId") BigInteger companyId,
                                  @RequestParam(value = "driverId") BigInteger driverId,
                                  @RequestParam(value = "carId") BigInteger carId,
                                  @RequestParam(value = "isDeleted") Integer isDeleted) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        examineRemarks = examineRemarks.trim();
        deliveryAddress = deliveryAddress.trim();
        arrivalAddress = arrivalAddress.trim();
        remarks = BaseUtil.isEmpty(remarks)?null:remarks.trim();
        try {
            baseWaybillService.waybillEdit(id, carId, driverId, clientId, companyId, typeOfGoods, weightOfGoods, volumeOfGoods,
                    deliveryTime, deliveryProvince, deliveryCity, deliveryRegion, deliveryAddress, arrivalTime, arrivalProvince,
                    arrivalCity, arrivalRegion, arrivalAddress, remarks, examineRemarks,
                    examineStatus,loginUser.getId(),BaseUtil.currentSeconds(),isDeleted);
            return new Response(1001);
        } catch (RuntimeException e) {
            log.info("Error WaybillUpdate:"+e.getMessage());
            return new Response(2003);
        }
    }

    /**
     * 待审核运单列表展示
     */
    @RequestMapping("/waybill/list")
    public Response WaybillList(@VerifiedUser User loginUser,
                                @RequestParam(value = "page") Integer page,
                                @RequestParam(value = "companyName", required = false) String companyName,
                                @RequestParam(value = "clientName", required = false) String clientName,
                                @RequestParam(value = "driverName", required = false) String driverName,
                                @RequestParam(value = "carNumberPlate", required = false) String carNumberPlate) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        companyName = BaseUtil.isEmpty(companyName)?null:companyName.trim();
        clientName = BaseUtil.isEmpty(clientName)?null:clientName.trim();
        driverName = BaseUtil.isEmpty(driverName)?null:driverName.trim();
        carNumberPlate = BaseUtil.isEmpty(carNumberPlate)?null:carNumberPlate.trim();
        List<Waybill> waybillList=new ArrayList<>();
        try {
            waybillList = baseWaybillService.getWaybillList(page, pageSize, companyName, clientName, driverName, carNumberPlate);
        }catch (RuntimeException e){
            log.info("Error getWaybillList:"+e.getMessage());
            return new Response(2005);
        }
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
        Map<BigInteger, String> driverPhones = drivers.stream().collect(Collectors.toMap(Driver::getId, Driver::getPhone));
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
            entity.setDriverPhone(driverPhones.get(w.getDriverId()));
            entity.setCarNumberPlate(carNumberPlates.get(w.getCarId()));
            entity.setDeliveryTime(BaseUtil.timeStamp2Date(w.getDeliveryTime()));
            entity.setArrivalTime(BaseUtil.timeStamp2Date(w.getArrivalTime()));
            list.add(entity);
        }
        try{
            waybillListVO.setTotal(baseWaybillService.getWaybillListTotal(companyName, clientName, driverName, carNumberPlate));
        }catch (RuntimeException e){
            log.info("Error getWaybillListTotal:"+e.getMessage());
            return new Response(2005);
        }
        waybillListVO.setPageSize(pageSize);
        waybillListVO.setList(list);
        return new Response(1001, waybillListVO);

    }

    /**
     * 待审核运单详情
     */
    @RequestMapping("/waybill/info")
    public Response WaybillInfo(@VerifiedUser User loginUser, @RequestParam(value = "id") BigInteger id) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
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
        waybillInfoVO.setCreateTime(BaseUtil.timeStamp2Date(waybill.getCreateTime()));
        waybillInfoVO.setUpdateTime(BaseUtil.timeStamp2Date(waybill.getUpdateTime()));
        return new Response(1001, waybillInfoVO);
    }

    /**
     * 运单审核
     */
    @RequestMapping("/waybill/examine")
    public Response WaybillExamine(@VerifiedUser User loginUser,
                                   @RequestParam(value = "id") BigInteger id,
                                   @RequestParam(value = "examineStatus") Integer examineStatus,
                                   @RequestParam(value = "examineRemarks", required = false) String examineRemarks) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        examineRemarks = BaseUtil.isEmpty(examineRemarks) ? null : examineRemarks.trim();
        try{
            baseWaybillService.examine(id, examineStatus, examineRemarks,loginUser.getId(),BaseUtil.currentSeconds()).toString();
            return new Response(1001);
        }catch (RuntimeException e){
            log.info("Error WaybillExamine:"+e.getMessage());
            return new Response(2011);
        }


    }
    /**
     * 车辆select框
     */
    @RequestMapping("/select/car")
    public Response SelectCar(@VerifiedUser User loginUser,
                              @RequestParam(value = "carNumberPlate",required = false) String carNumberPlate
                              ){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        carNumberPlate=BaseUtil.isEmpty(carNumberPlate)?null:carNumberPlate.trim();
        List<Car> bySelect = carService.getBySelect(carNumberPlate);
        List<CarSelectVO> list=new ArrayList<>();
        for (Car c: bySelect) {
            CarSelectVO base=new CarSelectVO();
            base.setId(c.getId());
            base.setCarNumberPlate(c.getNumberPlate());
            list.add(base);
        }
        return new Response(1001,list);
    }
    /**
     * 司机select框
     */
    @RequestMapping("/select/driver")
    public Response SelectDriver(@VerifiedUser User loginUser,
                              @RequestParam(value = "name",required = false)String name,
                              @RequestParam(value = "phone",required = false)String phone){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=BaseUtil.isEmpty(name)?null:name.trim();
        phone=BaseUtil.isEmpty(phone)?null:phone.trim();
        List<Driver> bySelect = driverService.getBySelect(name,phone);
        List<DriverSelectVO> list=new ArrayList<>();
        for (Driver d:bySelect){
            DriverSelectVO base=new DriverSelectVO();
            base.setId(d.getId());
            base.setName(d.getName());
            base.setPhone(d.getPhone());
            list.add(base);
        }
        return new Response(1001,list);
    }
    /**
     * 托运方select框
     */
    @RequestMapping("/select/client")
    public Response SelectClient(@VerifiedUser User loginUser,
                                 @RequestParam (value = "name",required = false)String name){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name=BaseUtil.isEmpty(name)?null:name.trim();
        List<Client> bySelect = clientService.getBySelect(name);
        List<ClientSelectVO> list=new ArrayList<>();
        for (Client d:bySelect){
            ClientSelectVO base=new ClientSelectVO();
            base.setId(d.getId());
            base.setName(d.getName());
            list.add(base);
        }
        return new Response(1001,list);
    }
    /**
     * 承运方select框
     */
    @RequestMapping("/select/company")
    public Response SelectCompany(@VerifiedUser User loginUser,
                                 @RequestParam (value = "name",required = false)String name) {
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        name = BaseUtil.isEmpty(name) ? null : name.trim();
        List<Company> bySelect = companyService.getBySelect(name);
        List<CompanySelectVO> list = new ArrayList<>();
        for (Company d : bySelect) {
            CompanySelectVO base = new CompanySelectVO();
            base.setId(d.getId());
            base.setName(d.getName());
            list.add(base);
        }
        return new Response(1001, list);
    }
    /**
     * 已审核运单列表
     */
    @RequestMapping("/waybill/snapshot/list")
    public Response WaybillSnapshotList(@VerifiedUser User loginUser,
                                        @RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "clientName", required = false) String clientName,
                                        @RequestParam(value = "clientPhone", required = false) String clientPhone,
                                        @RequestParam(value = "driverName", required = false) String driverName,
                                        @RequestParam(value = "carNumberPlate", required = false) String carNumberPlate){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
        clientName=BaseUtil.isEmpty(clientName)?null:clientName.trim();
        clientPhone=BaseUtil.isEmpty(clientPhone)?null:clientPhone.trim();
        driverName=BaseUtil.isEmpty(driverName)?null:driverName.trim();
        carNumberPlate=BaseUtil.isEmpty(carNumberPlate)?null:carNumberPlate.trim();
        List<WaybillSnapshot> list = waybillSnapshotService.getList(clientName, clientPhone, driverName, carNumberPlate, page, pageSize);
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
        waybillSnapshotListVO.setTotal(waybillSnapshotService.getListTotal(clientName, clientPhone, driverName, carNumberPlate));
        waybillSnapshotListVO.setPageSize(pageSize);
        waybillSnapshotListVO.setList(listVo);
        return new Response(1001, waybillSnapshotListVO);
    }
    /**
     * 已审核运单详情
     */
    @RequestMapping("/waybill/snapshot/info")
    public Response WaybillSnapshotInfo(@VerifiedUser User loginUser,
                                        @RequestParam(value = "id")BigInteger id){
        if (BaseUtil.isEmpty(loginUser)) {
            return new Response(1002);
        }
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
        waybillSnapshotVO.setCreateTime(BaseUtil.timeStamp2Date(waybillSnapshot.getCreateTime()));
        waybillSnapshotVO.setUpdateTime(BaseUtil.timeStamp2Date(waybillSnapshot.getUpdateTime()));
        return new Response(1001,waybillSnapshotVO);
    }

}
