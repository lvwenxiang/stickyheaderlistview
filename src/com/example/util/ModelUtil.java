package com.example.util;

import java.util.ArrayList;
import java.util.List;

import com.example.mode.OperationEntity;
import com.example.mode.TravelingEntity;

public class ModelUtil {
	  private static ArrayList<String> transformerList = new ArrayList<String>();

    public static final String type_scenery = "�羰";
    public static final String type_building = "����";
    public static final String type_animal = "����";
    public static final String type_plant = "ֲ��";
  

static{
	    
/*	transformerList.add(DefaultTransformer.class.getSimpleName());
    transformerList.add(AccordionTransformer.class.getSimpleName());
    transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
    transformerList.add(CubeInTransformer.class.getSimpleName());
    transformerList.add(CubeOutTransformer.class.getSimpleName());
    transformerList.add(DepthPageTransformer.class.getSimpleName());
    transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
    transformerList.add(FlipVerticalTransformer.class.getSimpleName());
    transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
    transformerList.add(RotateDownTransformer.class.getSimpleName());
    transformerList.add(RotateUpTransformer.class.getSimpleName());
    transformerList.add(StackTransformer.class.getSimpleName());
    transformerList.add(ZoomInTransformer.class.getSimpleName());
    transformerList.add(ZoomOutTranformer.class.getSimpleName());*/
}
//    //���ַ�ҳЧ��
    


    
    
    
    // �������
    public static List<String> getAdData() {
        List<String> adList = new ArrayList<>();
        adList.add("http://img0.imgtn.bdimg.com/it/u=1270781761,1881354959&fm=21&gp=0.jpg");
        adList.add("http://img0.imgtn.bdimg.com/it/u=2138116966,3662367390&fm=21&gp=0.jpg");
        adList.add("http://img0.imgtn.bdimg.com/it/u=1296117362,655885600&fm=21&gp=0.jpg");
        adList.add("http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg");
        adList.add("http://img2.3lian.com/2014/f2/37/d/40.jpg");
        adList.add("http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg");
        adList.add("http://img2.3lian.com/2014/f2/37/d/39.jpg");
        adList.add("http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg");
        adList.add("http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg");
        adList.add("http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg");

        return adList;
    }
	
	// ListView����
    public static List<TravelingEntity> getTravelingData() {
        List<TravelingEntity> travelingList = new ArrayList<TravelingEntity>();
        travelingList.add(new TravelingEntity(type_scenery, "����", "�й�", 1, "http://img2.3lian.com/2014/f2/37/d/40.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "", "������", 20, "http://img1.imgtn.bdimg.com/it/u=1832737924,144748431&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "", "�����", 21, "http://img5.imgtn.bdimg.com/it/u=2091366266,1524114981&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "����", "����", 5, "http://img4.imgtn.bdimg.com/it/u=3673198446,2175517238&fm=206&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_plant, "�ɻ�", "�й�", 4, "http://img4.imgtn.bdimg.com/it/u=3052089044,3887933556&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_building, "", "������", 18, "http://img2.imgtn.bdimg.com/it/u=140083303,1086773509&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "", "������", 19, "http://img5.imgtn.bdimg.com/it/u=1424970962,1243597989&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_animal, "ˮ��", "����", 7, "http://img4.imgtn.bdimg.com/it/u=1387833256,3665925904&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_plant, "������", "����", 8, "http://img1.imgtn.bdimg.com/it/u=3808801622,1608105009&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "����ʿ", "Ӣ��", 9, "http://img4.imgtn.bdimg.com/it/u=2440866214,1867472386&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_building, "�׶�����", "Ӣ��", 10, "http://img3.imgtn.bdimg.com/it/u=3040385967,1031044866&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_animal, "", "Ӣ��", 11, "http://img3.imgtn.bdimg.com/it/u=1896821840,3837942977&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_plant, "", "Ӣ��", 12, "http://img3.imgtn.bdimg.com/it/u=2745651862,279304559&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "", "�¹�", 13, "http://img3.imgtn.bdimg.com/it/u=4137420324,1489843447&fm=206&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_building, "����Ů����", "����", 6, "http://img3.imgtn.bdimg.com/it/u=2566161363,1140447270&fm=206&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_building, "����", "�й�", 2, "http://img1.imgtn.bdimg.com/it/u=372954611,2699392190&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_animal, "��è", "�й�", 3, "http://img0.imgtn.bdimg.com/it/u=1022702848,645282860&fm=206&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_building, "", "�¹�", 14, "http://img1.imgtn.bdimg.com/it/u=3436675019,2609348874&fm=206&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_animal, "����", "�¹�", 15, "http://img4.imgtn.bdimg.com/it/u=4280994062,276434784&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_plant, "", "�¹�", 16, "http://img0.imgtn.bdimg.com/it/u=1644854868,3172549858&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_scenery, "", "������", 17, "http://img4.imgtn.bdimg.com/it/u=620137884,621556624&fm=21&gp=0.jpg"));
        travelingList.add(new TravelingEntity(type_building, "", "�����", 22, "http://img0.imgtn.bdimg.com/it/u=3631118072,1530723002&fm=206&gp=0.jpg"));
        return travelingList;
    }
    // ��Ӫ����
    public static List<OperationEntity> getOperationData() {
        List<OperationEntity> operationList = new ArrayList<>();
        operationList.add(new OperationEntity("�ȼ���", "�ȼٵ�����", "http://img2.3lian.com/2014/f2/37/d/40.jpg"));
        operationList.add(new OperationEntity("������", "�����ĸ���", "http://img0.imgtn.bdimg.com/it/u=3631118072,1530723002&fm=206&gp=0.jpg"));
        return operationList;
    }
    
}
