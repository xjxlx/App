// OnChangeListenerAidlInterface.aidl
package com.android.app;

interface OnChangeListenerAidlInterface {

               /**
                 *  flag 1:吸气，2：屏住呼吸,3:开始呼气
                 */
                void onChange(in int flag);
}