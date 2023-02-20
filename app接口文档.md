# app接口文档

bassURL:http://localhost:8080

## 1，车辆列表

/car/list

### 参数

page:[int]
numberPlate:[String]//车牌号,模糊查询，可以没有
enterpriseId[Int],//公司id,模糊查询，可以没有

### 返回

```java
{
    isEnd:[boolean]
	list:[
		{
		    id:[int],
            numberPlate:[String],//车牌
            model:[String],//车型
            enterpriseId:[String],//公司名称
            businessStatus:[String],//业务状态
            examineStatus:[String],//审核状态
		}
	]
}
```

## 2,车辆详情

/car/info

### 参数

carId:[Int]

### 返回

```java
{
	id:[Int],
	numberplate:[String],//车牌
	model:[String],//车型
	enterpriseId:[String],//公司名称
	transport:[String],//运输证号
	trailer:[String],//挂车牌号（可以不传）
	businessStatus:[String],//业务状态
	examineStatus:[String],//审核状态
	licenseFrontPic:{//行驶证正面
             src:[String],
    		ar:[double]},//宽除以高，浮点数
	licenseBackPic:{
             src:[String],
    		ar:[double]},//行驶证反面
	transportPic:{
             src:[String],
    		ar:[double]},//运输证照片
	trailerPic:{
             src:[String],
    		ar:[double]},//挂车行驶证照片（可以不传）
}
```

## 3，用户登录

/user/login/app

### 参数

phone:[String]
password:[String]

### 返回

```java
userInfo：{
    	userId:[int],
        name:[String],//名称
        gender:[int],//性别
        phone:[String],//电话号码
        avatar:[String]//用户头像url
}
sign:[String]
```

## 4，用户注册

/user/register/app

### 参数

phone:[String],
gender:[int],//性别
avatar:[String],//用户头像，可以没有
name:[String],
password:[String],
country:[String],//可以没有
province:[String],//可以没有
city:[String],//可以没有

### 返回

```java
userInfo：{
    	userId:[int],
        name:[String],//名称
        gender:[int],//性别
        phone:[String],//电话号码
        avatar:[String]//用户头像url
}
sign:[String]
```

