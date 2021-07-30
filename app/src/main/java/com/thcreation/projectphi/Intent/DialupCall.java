package com.thcreation.projectphi.Intent;

import android.content.Intent;
import android.net.Uri;

public class DialupCall {
    public Intent Call(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0776068690"));
        return intent;
    }
}
