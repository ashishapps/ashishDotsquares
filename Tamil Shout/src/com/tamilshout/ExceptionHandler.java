package com.tamilshout;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.content.Intent;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;

    public ExceptionHandler(Context context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);

        Intent intent = new Intent(myContext, ForceClose.class);
        intent.putExtra("crash", stackTrace.toString());
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}