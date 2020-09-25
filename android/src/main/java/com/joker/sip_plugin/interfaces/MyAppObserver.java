package com.joker.sip_plugin.interfaces;

import com.joker.sip_plugin.entity.MyBuddy;
import com.joker.sip_plugin.entity.MyCall;

import org.pjsip.pjsua2.pjsip_status_code;

/**
 * Description:
 * Author: Jack Zhang
 * create on: 2019-08-12 14:28
 */
public interface MyAppObserver
{
  void notifyRegState(pjsip_status_code code, String reason, int expiration);

  void notifyIncomingCall(MyCall call);

  void notifyCallState(MyCall call);

  void notifyCallMediaState(MyCall call);

  void notifyBuddyState(MyBuddy buddy);

  void notifyChangeNetwork();
}
