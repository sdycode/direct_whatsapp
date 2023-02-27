
import 'dart:async';

import 'package:flutter/services.dart';
class DirectWhatsapp { static const MethodChannel _channel = MethodChannel('direct_whatsapp');
  Future<String?> getPlatformVersion()async {
    return       " d";
      // DirectWhatsappPlatform.instance.getPlatformVersion();
  }
  static Future<void> shareTextDirectToWhatsapp(int countryCode, String phoneNo, String message )async{
    await _channel.invokeMethod('shareTextDirectToWhatsapp', {"countryCode":countryCode,
      "phoneNo":phoneNo,
      "message":message});

  }

  static Future<void> shareImageToWhatsApp(String imgPath,[String? caption])async{
    await _channel.invokeMethod('shareImageToWhatsApp', {"path":imgPath,"caption":caption??""});

  }
  static Future<void> shareImage(String imgPath,[String? caption])async{
    await _channel.invokeMethod('shareImage', {"path":imgPath,"caption":caption??""});

  }


  static Future<void> shareMultipleImagesToWhatsApp(List<String> imgPathsList, [String? caption] )async {
    await _channel.invokeMethod('shareMultipleImagesToWhatsApp', {"paths":imgPathsList, "caption":caption??""});
  }static Future<void> shareMultipleImages(List<String> imgPathsList, [String? caption] )async {
    await _channel.invokeMethod('shareMultipleImages', {"paths":imgPathsList, "caption":caption??""});
  }
}
