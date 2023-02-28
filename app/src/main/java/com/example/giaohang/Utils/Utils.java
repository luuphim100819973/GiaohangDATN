package com.example.giaohang.Utils;
import android.app.Activity;
import com.example.giaohang.Objects.TypeObject;
import com.example.giaohang.R;
import java.math.BigDecimal;
import java.util.ArrayList;
public class Utils {

    /**Loại xe chạy
     */
    public static ArrayList<TypeObject> getTypeList(Activity activity){
        ArrayList<TypeObject> typeArrayList = new ArrayList<>();
        typeArrayList.add(new TypeObject("type_1", activity.getResources().getString(R.string.type_1), activity.getResources().getDrawable(R.drawable.ic_type_4), 1));
      //  typeArrayList.add(new TypeObject("type_2", activity.getResources().getString(R.string.type_2), activity.getResources().getDrawable(R.drawable.ic_type_2), 7));
        typeArrayList.add(new TypeObject("type_2", activity.getResources().getString(R.string.type_2), activity.getResources().getDrawable(R.drawable.ic_type_1), 1));
        return  typeArrayList;
    }
    /**
     * biến id - id object cần tìm
     * biến - type object
     */
    public static TypeObject getTypeById(Activity activity, String id){
        ArrayList<TypeObject> typeArrayList = getTypeList(activity);
        for(TypeObject mType : typeArrayList){
            if(mType.getId().equals(id)){
                return mType;
            }
        }
        return null;
    }
}
