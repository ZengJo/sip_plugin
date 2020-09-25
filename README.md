# sip_plugin

flutter sip plugin

## Flutter调原生信号通道
  信号通道名称
 ```javascript
 /** 信号通道*/
 flutter_pjsip
  ```
## Flutter 传值给原生方法名
方法名
 ```javascript
1./** pjsip初始化*/
method_pjsip_init
2./** pjsip登录*/
method_pjsip_login
3./** pjsip拨打电话*/
method_pjsip_call
4./** pjsip登出*/
method_pjsip_logout
5./** pjsip销毁*/
method_pjsip_deinit
6./** 接收电话*/
method_pjsip_receive
7./** 挂断&&拒接*/
method_pjsip_refuse
8./** 免提*/
method_pjsip_audioSession
9./** 静音*/
method_pjsip_muteMicrophone
 ```

## 原生调Flutter信号通道
  信号通道名称
 ```javascript
 /** 信号通道*/
 native_pjsip
  ```


 ##   原生传值给Flutter

  PJSIP里所有的回调状态
 ```javascript
 /**
 * This enumeration describes invite session state.
 */
typedef enum pjsip_inv_state
{
    PJSIP_INV_STATE_NULL,	    /**< Before INVITE is sent or received  */
    PJSIP_INV_STATE_CALLING,	    /**< After INVITE is sent		    */
    PJSIP_INV_STATE_INCOMING,	    /**< After INVITE is received.	    */
    PJSIP_INV_STATE_EARLY,	    /**< After response with To tag.	    */
    PJSIP_INV_STATE_CONNECTING,	    /**< After 2xx is sent/received.	    */
    PJSIP_INV_STATE_CONFIRMED,	    /**< After ACK is sent/received.	    */
    PJSIP_INV_STATE_DISCONNECTED,   /**< Session is terminated.		    */
} pjsip_inv_state;
  ```
这里原生会以json串的形式传值给Flutter，Flutter会根据不同的value值处理相应的事件
 ```javascript
例如：{  callStatusChanged: @(PJSIP_INV_STATE_CONNECTING) }
这里对应的key值有七个状态，都包含：
   PJSIP_INV_STATE_NULL,	    		/**< 在发送或接收邀请之前  */
   PJSIP_INV_STATE_CALLING,	   		 /**< 发送邀请后		    */
   PJSIP_INV_STATE_INCOMING,		 /**< 来电中	    */
   PJSIP_INV_STATE_EARLY,	   		 /**< 来电之前	    */
   PJSIP_INV_STATE_CONNECTING,	    /**响铃中	    */
   PJSIP_INV_STATE_CONFIRMED,	    /**<已确认	    */
   PJSIP_INV_STATE_DISCONNECTED,   /**已挂断或已拒绝    */
  ```

## PJSIP插件iOS端集成说明
1.配置本地依赖库
> CoreTelephony.framework
>AVFoundation.framework

2	在APPdelegate里引入头文件并调用

 ```javascript
#include "AppDelegate.h"
#include "GeneratedPluginRegistrant.h"
#import "MainViewController.h"
#import "FlutterAppDelegate+Pjsip.h" /** 需要导入的头文件*/
@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [GeneratedPluginRegistrant registerWithRegistry:self];
    /** 设置主控制器继承FlutterViewController*/
    MainViewController * VC = [[MainViewController alloc]init];
    UINavigationController * NVC = [[UINavigationController alloc]initWithRootViewController:VC];
    [self.window setRootViewController:NVC];
    [self setupPjsip:application rootController:VC];/** 需要调用的方法*/
  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}
@end
 ```
 3.运行cocoaPods
> pod install --verbose --no-repo-update

4.手动配置flutter的flutter_pjsip.xcconfig文件

需要保证flutter_pjsip.xcconfig文件与Pods-Runner.debug.xcconfig文件的`'GCC_PREPROCESSOR_DEFINITIONS'`和`'HEADER_SEARCH_PATHS'` 的配置高度一致，否者会报#include <pjsua-lib/pjsua.h>头文件找不到的错。

## Flutter 使用示例
```
import 'dart:async';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:sip_plugin/sip_plugin.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _calltateText = '';
  SipPlugin _pjsip;

  @override
  void initState() {
    super.initState();
    initSipPlugin();
  }

  void initSipPlugin() {
    _pjsip = SipPlugin.instance;
    _pjsip.onSipStateChanged.listen((map) {
      final state = map['call_state'];
      final remoteUri = map['remote_uri'];
      print('收到状态: $state=====$remoteUri');
      switch (state) {
        case "CALLING":
          break;

        case "INCOMING":
          break;

        case "EARLY":
          break;

        case "CONNECTING":
          break;

        case "CONFIRMED":
          break;

        case "DISCONNECTED":
          break;

        default:
          break;
      }

      setState(() {
        this._calltateText = state;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            RaisedButton(
              child: Text('Sip初始化'),
              onPressed: () => _sipInit(),
            ),
            RaisedButton(
              child: Text('Sip登录'),
              onPressed: () => _sipLogin(),
            ),
            RaisedButton(
              child: Text('Sip打电话'),
              onPressed: () => _sipCall(),
            ),
            RaisedButton(
              child: Text('Sip登出'),
              onPressed: () => _sipLogout(),
            ),
            RaisedButton(
              child: Text('Sip销毁'),
              onPressed: () => _sipDeinit(),
            ),
            RaisedButton(
              child: Text('Sip接听'),
              onPressed: () => _sipReceive(),
            ),
            RaisedButton(
              child: Text('Sip拒接/挂断'),
              onPressed: () => _sipRefuse(),
            ),
            RaisedButton(
              child: Text('Sip免提'),
              onPressed: () => _sipHandsFree(),
            ),
            RaisedButton(
              child: Text('Sip静音'),
              onPressed: () => _sipMute(),
            ),
            RaisedButton(
              child: Text('Sip通道销毁'),
              onPressed: () => _sipDispose(),
            ),
            Text('电话状态监听：$_calltateText'),
          ],
        ),
      ),
    );
  }

  Future<void> _sipInit() async {
    bool initSuccess = await _pjsip.pjsipInit();
    showToast('初始化', initSuccess);
  }

  Future<void> _sipLogin() async {
    bool loginSuccess = await _pjsip.pjsipLogin(
        username: '215-217',
        password: 'qq1235',
        ip: '47.115.52.203',
        port: '5060');
    showToast('登录', loginSuccess);
  }

  Future<void> _sipCall() async {
    bool callSuccess = await _pjsip.pjsipCall(
        username: '215-217', ip: '47.115.52.203', port: '5060');
    showToast('打电话', callSuccess);
  }

  Future<void> _sipLogout() async {
    bool logoutSuccess = await _pjsip.pjsipLogout();
    showToast('登出', logoutSuccess);
  }

  Future<void> _sipDeinit() async {
    bool initSuccess = await _pjsip.pjsipDeinit();
    showToast('销毁', initSuccess);
  }

  Future<void> _sipReceive() async {
    bool receiveSuccess = await _pjsip.pjsipReceive();
    showToast('接听', receiveSuccess);
  }

  Future<void> _sipRefuse() async {
    bool refuseSuccess = await _pjsip.pjsipRefuse();
    showToast('拒接/挂断', refuseSuccess);
  }

  Future<void> _sipHandsFree() async {
    bool handsFreeSuccess = await _pjsip.pjsipHandsFree();
    showToast('免提状态更改', handsFreeSuccess);
  }

  Future<void> _sipMute() async {
    bool muteSuccess = await _pjsip.pjsipMute();
    showToast('静音状态更改', muteSuccess);
  }

  Future<void> _sipDispose() async {
    await _pjsip.dispose();
    showToast('通道销毁', true);
  }

  void showToast(String method, bool success) {
    String successText = success ? '成功' : '失败';
    Fluttertoast.showToast(msg: '$method $successText');
  }
}

```


