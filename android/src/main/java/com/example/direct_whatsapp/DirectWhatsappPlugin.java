package com.example.direct_whatsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** DirectWhatsappPlugin */
public class DirectWhatsappPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "direct_whatsapp");
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("shareTextDirectToWhatsapp")) {
      String phoneNumberWithCountryCode = "+" + call.argument("countryCode") + call.argument("phoneNo");

      Log.d("", phoneNumberWithCountryCode);
      String message = call.argument("message");
      String whatsappapilink = "https://api.whatsapp.com/send?phone=%s&text=%s";
      Intent shareIntent = new Intent(Intent.ACTION_VIEW,
              Uri.parse(
                      String.format(whatsappapilink, phoneNumberWithCountryCode, message)
              )
      );
      shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(
              shareIntent
      );

    }
    else if (call.method.equals("shareImageToWhatsApp")) {
      String imgPath = (String) call.argument("path");
      String caption = (String) call.argument("caption");
      String providerAuthority = context.getApplicationContext().getPackageName() + ".flutter.share_provider";
      File file = new File(imgPath);
      Log.d("len", "len" + file.length());
      try {
        Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), providerAuthority, file);
        Log.d("uri", "uri" + uri.getPath());
        Intent sh = new Intent(Intent.ACTION_SEND,
                uri
        );

        sh.putExtra(Intent.EXTRA_STREAM, uri);

        sh.setPackage("com.whatsapp");
        sh.putExtra(Intent.EXTRA_TEXT, caption);
        sh.setType("image/png");
        sh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sh);

      } catch (Exception e) {
        Log.e("err", "err " + e);
      }


    }

    else if(call.method.equals("shareImage")){
      String imgPath = (String) call.argument("path");
      String caption = (String) call.argument("caption");
      String providerAuthority = context.getApplicationContext().getPackageName() + ".flutter.share_provider";
      File file = new File(imgPath);
      Log.d("len", "len" + file.length());
      try {
        Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), providerAuthority, file);
        Log.d("uri", "uri" + uri.getPath());
        Intent sh = new Intent(Intent.ACTION_SEND,
                uri
        );

        sh.putExtra(Intent.EXTRA_STREAM, uri);


        sh.putExtra(Intent.EXTRA_TEXT, caption);
        sh.setType("image/*");
        sh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sh);

      } catch (Exception e) {
        Log.e("err", "err " + e);
      }

    }
    else if (call.method.equals("shareMultipleImagesToWhatsApp"))        {
      ArrayList<String> imgPaths = (ArrayList<String>) call.argument("paths");
      String caption = (String) call.argument("caption");
      ArrayList<Uri> uriList = new ArrayList<Uri>();
      uriList.clear();
      String providerAuthority = context.getApplicationContext().getPackageName() + ".flutter.share_provider";

      for (int i = 0; i < imgPaths.size(); i++) {
        File file = new File(imgPaths.get(i));
        try {
          Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), providerAuthority, file);
          Log.d("uri", "uri" + uri.getPath());
          uriList.add(uri);
        } catch (Exception e) {
          Log.e("err", "err " + e);
        }
      }
      Intent sh = new Intent();
      sh.setType("*/*");
      sh.setAction(Intent.ACTION_SEND_MULTIPLE);
      sh.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
      sh.setPackage("com.whatsapp");
      sh.putExtra(Intent.EXTRA_TEXT, caption);
      sh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(sh);

    }
    else if (call.method.equals("shareMultipleImages")) {
      ArrayList<String> imgPaths = (ArrayList<String>) call.argument("paths");
      String caption = (String) call.argument("caption");
      ArrayList<Uri> uriList = new ArrayList<Uri>();
      uriList.clear();
      String providerAuthority = context.getApplicationContext().getPackageName() + ".flutter.share_provider";

      for (int i = 0; i < imgPaths.size(); i++) {
        File file = new File(imgPaths.get(i));
        try {
          Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), providerAuthority, file);
          Log.d("uri", "uri" + uri.getPath());
          uriList.add(uri);
        } catch (Exception e) {
          Log.e("err", "err " + e);
        }
      }
      Intent sh = new Intent();
      sh.setType("*/*");
      sh.setAction(Intent.ACTION_SEND_MULTIPLE);
      sh.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);

      sh.putExtra(Intent.EXTRA_TEXT, caption);
      sh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent.createChooser(sh);
      context.startActivity(sh);

    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
