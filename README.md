# Quick-Add-Products
an Android app that use Retrofit (API) to add new product to opencart
## Introduction
This android app is for adding products quickly in E-commerce CMS (Opencart).

products will added with a picture taken by your device, Name, Price, Quantity and Mode and the status of Product will be Disabled . 

This is great for you to add all your products quickly in your store to your CMS and easily have your product list with brief Information to help complete them.

**To use this app** first make server side capable for interaction with app through API.for this purpose i wrote module for opencart.

- download and install this module .
- **Go to** System -> Users -> API, then Add New with *API Username* same as *admin username* then copy *API Key* , add your *current IP Address* by IP Address Tab then save it.

![Add-API-User](http://lightg.ir/image/API-user.png)

![Add-IP Address](http://lightg.ir/image/AddIpAddress.png)

- In android app source code open **Login.java** and replace the value of *private String api_key* with the *Api-key* that you copied from opencart api user.
- In android app source code open **Login.java** and **MainActivity.java** replace the value of *baseUrl("http.....")* for Retrofit.Builder() with your *Domin Name*.
- make your APK file and install it on your device.
- open app hit the log in floating action button and login with administrator user and password this makes opencart to start new api token for you and send back it to your app.
- take picture and fill the fields the hit to add product.

