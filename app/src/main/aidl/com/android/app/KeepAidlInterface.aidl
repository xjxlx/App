// KeepAidlInterface.aidl
package com.android.app;
import com.android.app.OnChangeListenerAidlInterface;
// Declare any non-default types here with import statements
interface KeepAidlInterface {

  void setOnChangeListener(OnChangeListenerAidlInterface stub);

}