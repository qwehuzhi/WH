# console接口文档

bassURL:http://localhost:8081

## 1，车辆新增接口

/car/add

### 参数

```java
numberPlate:[String],//车牌
model:[String],//车型
transport:[String],//运输证号
trailer:[String],//挂车牌号（可以不传）
licenseFrontPic:[String],//行驶证正面
licenseBackPic:[String],//行驶证反面
transportPic:[String],//运输证照片
trailerPic:[String],//挂车行驶证照片（可以不传）
businessStatus:[String],//业务状态
enterpriseId:[int],//公司id
tagList:[tag[String]],//标签,用数组传(可以不传)
```



### 返回

成功or失败

## 2，车辆修改接口

/car/update

### 参数

```java
carId:[int],
numberplate:[String],//车牌
model:[String],//车型
transport:[String],//运输证号
trailer:[String],//挂车牌号（可以不传）
businessStatus:[String],//业务状态
examineStatus:[String],//审核状态
licenseFrontPic:[String],//行驶证正面
licenseBackPic:[String],//行驶证反面
transportPic:[String],//运输证照片
trailerPic:[String],//挂车行驶证照片（可以不传）
enterpriseId:[int],//公司id
list:[tag[String]],//标签,用数组传(可以不传)
```



#### 返回

成功or失败

## 3，车辆删除接口

/car/delete

### 参数

carId:[Int]

### 返回

成功or失败

## 4，车辆列表接口

/car/list

### 参数

```java
page:[int],
tag:[String],//标签，模糊查询，可以没有
numberPlate:[String],//车牌号，模糊查询，可以没有
enterpriseId:[int],//公司id,模糊查询，可以没有
```



### 返回

```java
total:[long],//总数
pageSize:[int]，//每页大小
list:[
    {
		id:[Int],
		numberplate:[String],//车牌
		enterpriseId:[String],//公司名称
		businessStatus:[String],//业务状态
		examineStatus:[String],//审核状态
		isDeleted:[String]//是否被删除
    }
]
```

## 5，车辆详情接口

/car/info

### 参数

carId:[int]

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
	createTime:[String],//创建时间，需要转化成日期
	updateTime:[String],//最后修改时间，需要转化成日期
	isDeleted:[String]//是否被删除
}
```

## 6，图片上传接口

/image

### 参数

picture[file]

### 返回

```java
image:{
    src:[String],
    ar:[double]//宽除以高，浮点数
}
```



## 7，用户登录

/user/login/web

### 参数

phone:[String],//手机号
password:[String],//密码
remember:[int],//必须是1或者0

### 返回

```java
userInfo：{
		userId:[int],
		name:[String],//名称
		gender:[int],//性别
		phone:[String],//电话号码
		avatar:[String]//用户头像url
}
```

## 8，新增标签

/tag/insert

### 参数

tag[String],//标签

### 返回

tagId[int]//tag的id

## 9，删除标签

/tag/delete

### 参数

tagId[int]

### 返回

成功或失败
