package com.sl.pmpapp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sl.pmpapp.config.ClientUrl;
import com.sl.pmpapp.service.ElecReportDataService;
import com.sl.pmpapp.utils.CEUtils;
import com.sl.pmpapp.utils.CodeMsg;
import com.sl.pmpapp.utils.HttpClientService;
import com.sl.pmpapp.utils.JwtToken;
import com.sl.pmpapp.utils.ListSort;
import com.sl.pmpapp.utils.Result;



@Service("elecReportDataImpl")
public class ElecReportDataServiceImpl implements ElecReportDataService {
	
	
	@Autowired
	ClientUrl clientUrl;
	
	
	
	/**
	 * 一个项目的所有厂区+配电室
	 */
	@Override
	public Object get_project_factorys(Map<String, Object> map) {
		Map<String, Object> map3= new HashMap<String, Object>();   //返回的一条数据
		Map<String, Object> map4= new HashMap<String, Object>();   //向接口请求参数
		Map<String, Object> map5= new HashMap<String, Object>();   //
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		
		map4.put("project_id", JwtToken.getProject_id(map.get("token")+""));   //根据token获取project_id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_project_factorys";   //所有厂区
		HttpClientService hc=new HttpClientService();
		String data = hc.get(GET_URL,map4); //接口数据String类型
		
		Gson gson=new Gson();
		HashMap<String,Object> factoryData = gson.fromJson(data, HashMap.class); //接口数据
		HashMap<String,Object> factoryData2 = gson.fromJson(factoryData.get("data").toString(), HashMap.class);  //data数据
		for (Map.Entry<String, Object> entry : factoryData2.entrySet()) {  //data
			HashMap<String,Object> factoryData3 = gson.fromJson(entry.getValue().toString(), HashMap.class); //data的value
				    map3= new HashMap<String, Object>();
					map3.put("factory_name", factoryData3.get("factory_name"));
			    	double d1=Double.parseDouble(factoryData3.get("id").toString());
			    	Double D1=new Double(d1); 
			    	int i1=D1.intValue(); 
					map3.put("factory_id",i1);
					
					list.add(map3);
		}
		
		for(int i=0;i<list.size();i++){
			List<Map<String, Object>> list2=new ArrayList<>();    //data 
			map5=new HashMap<String, Object>();
			map5.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
			map5.put("column_key",true);  //是否key值索引.
			map5.put("factory_id",list.get(i).get("factory_id"));  //厂区id
			map5=CEUtils.pmpEncrypt(map5); //加密传输数据
			String GET_URL1 = clientUrl.PHPCENTER+"elecreporting/get_transformerroom_list";   //所选厂区电的区域分类列表
			String data1 = hc.get(GET_URL1,map5); //接口数据String类型
			HashMap<String,Object> factory_elecData = gson.fromJson(data1, HashMap.class);
			HashMap<String,Object> factory_elecData2 = gson.fromJson(factory_elecData.get("data").toString(), HashMap.class); //data
			for (Map.Entry<String, Object> entry2 : factory_elecData2.entrySet()) {  //data
				HashMap<String,Object> factory_elecData3 = gson.fromJson(entry2.getValue().toString(), HashMap.class); //data的value
							map5=new HashMap<String, Object>();
						    map5.put("category_name",factory_elecData3.get("category_name")); 		//区域名称
					    	double d1=Double.parseDouble(factory_elecData3.get("category_id").toString());
					    	Double D1=new Double(d1); 
					    	int i1=D1.intValue(); 
						    map5.put("category_id",i1); 		//区域ID
					    	double d2=Double.parseDouble(factory_elecData3.get("transformerroom_id").toString());
					    	Double D2=new Double(d2); 
					    	int i2=D2.intValue(); 
						    map5.put("rid",i2);
							list2.add( map5);
			}	
			list.get(i).put("category_list", list2);
		}
		
		return Result.success(list);
	}
	
	
	
	/**
	 * 单日分类列表 
	 */
	@Override
	public Object singleDay_list(Map<String, Object> map) {
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		//判断 时间
		if(map.get("date")==null||map.get("date")==""){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); //得到前一天
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			map.put("date", df.format(date));
		}
		
		//厂区 判断 以及 返回.
		Map<String, Object> fmap= new HashMap<String, Object>();   //返回数据
		List<Map<String, Object>> flist=new ArrayList<>(); 
		fmap.put("project_id", JwtToken.getProject_id(map.get("token")+""));   //根据token获取project_id
		fmap=CEUtils.pmpEncrypt(fmap); //加密传输数据
		String fGET_URL = clientUrl.PHPCENTER+"elecreporting/get_project_factorys";   //所有厂区
		String fdata = hc.get(fGET_URL,fmap); //接口数据String类型
		HashMap<String,Object> factoryData = gson.fromJson(fdata, HashMap.class); //接口数据
		HashMap<String,Object> factoryData2 = gson.fromJson(factoryData.get("data").toString(), HashMap.class);  //data数据
		for (Map.Entry<String, Object> entry : factoryData2.entrySet()) {  //data
			HashMap<String,Object> factoryData3 = gson.fromJson(entry.getValue().toString(), HashMap.class); //data的value
				    	fmap= new HashMap<String, Object>();
				    	fmap.put("factory_name", factoryData3.get("factory_name"));
				    	double d1=Double.parseDouble(factoryData3.get("id").toString());
				    	Double D1=new Double(d1); 
				    	int i1=D1.intValue(); 
				    	fmap.put("factory_id",i1);
				    	flist.add(fmap);
				    }
	
		if(map.get("factory_id")==null){     
			map.put("factory_id", flist.get(0).get("factory_id"));
		}
		Map<String, Object> fmap2= new HashMap<String, Object>();
		for(int i=0;i<flist.size();i++){
				if(flist.get(i).get("factory_id").toString().contains(map.get("factory_id").toString())){
					fmap2.put("factory_name", flist.get(i).get("factory_name"));
			    	fmap2.put("factory_id", flist.get(i).get("factory_id"));
				}
			}				
		
		
		//配电室  判断  以及 返回
		Map<String, Object> map5= new HashMap<String, Object>();   //接口数据
		Map<String, Object> map6= new HashMap<String, Object>();
		map5.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map5.put("category_type",2);  //分类类别id
		map5.put("column_key",true);  //是否key值索引.
		map5.put("factory_id",map.get("factory_id"));  //工厂id
		map5=CEUtils.pmpEncrypt(map5); //加密传输数据
		String GET_URL2 = clientUrl.PHPCENTER+"elecreporting/get_distribution_room_branch_list";   //所选厂区电的区域分类列表
		String data2 = hc.get(GET_URL2,map5); //接口数据String类型
		List<Map<String, Object>> list2=new ArrayList<>();    //data 多条数据
		HashMap<String,Object> bData1 = gson.fromJson(data2, HashMap.class);
		Map<String,Object> bData2=(Map<String, Object>) bData1.get("data");
		for (Map.Entry<String, Object> entry : bData2.entrySet()) {  //data
			Map<String,Object> bData3 = (Map<String, Object>) entry.getValue(); //data
			for (Map.Entry<String, Object> entry2 : bData3.entrySet()) {
				Map<String,Object> bData4 = (Map<String, Object>) entry2.getValue(); //data
					// if(bData4.get("category_id").equals(map.get("category_id"))){  //如果配电室id相等
				map6= new HashMap<String, Object>();
				map6.put("category_branch_id", bData4.get("category_branch_id"));
				map6.put("category_id",bData4.get("category_id"));
				map6.put("category_name", bData4.get("category_name"));
				list2.add(map6);  //
			}}
		
		if(map.get("category_id")==null||map.get("category_id")==""){
			map.put("category_id",list2.get(0).get("category_id"));
		}
		
		List list3=new ArrayList(); 
		Map<String, Object> rmap= new HashMap<String, Object>();
		for(int i=0;i<list2.size();i++){
				if(list2.get(i).get("category_id").equals(map.get("category_id"))||list2.get(i).get("category_id")==map.get("category_id")){
					rmap.put("category_name", list2.get(i).get("category_name"));
					rmap.put("category_id",Integer.parseInt(list2.get(i).get("category_id").toString()));
					list3.add(list2.get(i).get("category_branch_id"));
				}				
		}
		
		
		Map<String, Object> map4= new HashMap<String, Object>();   //接口数据
		map4.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map4.put("category_type",3);  //分类类别id
		map4.put("column_key",true);  //是否key值索引.
		map4.put("factory_id",map.get("factory_id"));  //工厂id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		String data = hc.get(GET_URL,map4); //接口数据String类型
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class);
		Map<String,Object> Data2=(Map<String, Object>) Data1.get("data");
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
		   Map<String,Object> Data3 = (Map<String, Object>) entry.getValue(); //data
						for(int i=0;i<list3.size();i++){
							if(list3.get(i).equals(Data3.get("category_id"))){
								 map3=new HashMap<String, Object>();
								 map3.put("branch_name",Data3.get("category_name")); 		//支路名称
							     map3.put("branch_id",Integer.parseInt(Data3.get("category_id").toString())); 		//支路ID
								 list.add( map3);
							}
						}
		}
		

			
			
		Map<String, Object> pmap= new HashMap<String, Object>();
		Map<String, Object> pmap2= new HashMap<String, Object>();
		List<Map<String, Object>> plist=new ArrayList<>(); 
		pmap.put("date_type", "days_data");
		pmap.put("date_from", map.get("date"));
		pmap.put("date_to", map.get("date"));
		
		for(int i=0;i<list.size();i++){
			pmap.put("meter_ids", list.get(i).get("branch_id"));
			pmap2=CEUtils.pmpEncrypt(pmap); //加密传输数据
			String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
			String pdata = hc.get(pGET_URL,pmap2); //接口数据String类型
			HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
			Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
			for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
				HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
				for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
					HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
					list.get(i).put("consumption_value",Double.parseDouble(pData4.get("power").toString()));
				}
			}
		
		}	
		
		int fana=0;
		int fanb=19;
		if(map.get("pageNum")==null||map.get("pageNum")==""||map.get("pageSize")==null||map.get("pageSize")==""){
			fana=0;
			fanb=19;
		}else{
			int pageNum=Integer.parseInt(map.get("pageNum").toString()); 
			int pageSize=Integer.parseInt(map.get("pageSize").toString());
			fana=(pageNum-1)*pageSize;
			fanb=pageNum*pageSize;
		}
		
		
		map2.put("is_lastpage", false);
		List<Map<String, Object>> fanlist=new ArrayList<>(); 
		for(int i=fana;i<fanb;i++){
			if(i>=list.size()){
				map2.put("is_lastpage", true);
				break;
			}
			fanlist.add(list.get(i));
		}
		
		map2.put("category_id", rmap.get("category_id"));
		map2.put("category_name", rmap.get("category_name"));
		map2.put("factory_name", fmap2.get("factory_name"));
		map2.put("factory_id", fmap2.get("factory_id"));
		map2.put("date", map.get("date"));
		map2.put("singleDay_list", fanlist);  //单日列表
		
		return Result.success(map2);
	}
	
	
	/**
	 * 区间分类列表 
	 */
	@Override
	public Object section_list(Map<String, Object> map) {
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		//判断 时间
		if(map.get("start_date")==null||map.get("start_date")==""){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1); //得到前一天
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			map.put("end_date", df.format(date));
			
			Calendar calendar2 = Calendar.getInstance();
			calendar2.add(Calendar.DATE, -8); //得到前一周
			Date date2 = calendar2.getTime();
			map.put("start_date", df.format(date2));
		}
		
		
		Map<String, Object> fmap= new HashMap<String, Object>();   //返回数据
		List<Map<String, Object>> flist=new ArrayList<>(); 
		fmap.put("project_id", JwtToken.getProject_id(map.get("token")+""));   //根据token获取project_id
		fmap=CEUtils.pmpEncrypt(fmap); //加密传输数据
		String fGET_URL = clientUrl.PHPCENTER+"elecreporting/get_project_factorys";   //所有厂区
		String fdata = hc.get(fGET_URL,fmap); //接口数据String类型
		HashMap<String,Object> factoryData = gson.fromJson(fdata, HashMap.class); //接口数据
		HashMap<String,Object> factoryData2 = gson.fromJson(factoryData.get("data").toString(), HashMap.class);  //data数据
		for (Map.Entry<String, Object> entry : factoryData2.entrySet()) {  //data
			HashMap<String,Object> factoryData3 = gson.fromJson(entry.getValue().toString(), HashMap.class); //data的value
				    	fmap= new HashMap<String, Object>();
				    	fmap.put("factory_name",factoryData3.get("factory_name"));
				    	
				    	double d1=Double.parseDouble(factoryData3.get("id").toString());
				    	Double D1=new Double(d1); 
				    	int i1=D1.intValue(); 
				    	fmap.put("factory_id",i1);
				    	flist.add(fmap);
				    }
		
		if(map.get("factory_id")==null){     
			map.put("factory_id",flist.get(0).get("factory_id"));
		}
		Map<String, Object> fmap2= new HashMap<String, Object>();
		for(int i=0;i<flist.size();i++){
				if(flist.get(i).get("factory_id").toString().contains(map.get("factory_id").toString())||flist.get(i).get("factory_id")==map.get("factory_id")){
					
					fmap2.put("factory_name",flist.get(i).get("factory_name"));
			    	fmap2.put("factory_id", flist.get(i).get("factory_id"));
				}
			}
		
		
		
		Map<String, Object> map5= new HashMap<String, Object>();   //接口数据
		Map<String, Object> map6= new HashMap<String, Object>();
		map5.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map5.put("category_type",2);  //分类类别id
		map5.put("column_key",true);  //是否key值索引.
		map5.put("factory_id",map.get("factory_id"));  //工厂id
		map5=CEUtils.pmpEncrypt(map5); //加密传输数据
		String GET_URL2 = clientUrl.PHPCENTER+"elecreporting/get_distribution_room_branch_list";   //所选厂区电的区域分类列表
		String data2 = hc.get(GET_URL2,map5); //接口数据String类型
		List<Map<String, Object>> list2=new ArrayList<>();    //data 多条数据
		HashMap<String,Object> bData1 = gson.fromJson(data2, HashMap.class);
		Map<String,Object> bData2=(Map<String, Object>) bData1.get("data");
		for (Map.Entry<String, Object> entry : bData2.entrySet()) {  //data
			Map<String,Object> bData3 = (Map<String, Object>) entry.getValue(); //data
			for (Map.Entry<String, Object> entry2 : bData3.entrySet()) {
				Map<String,Object> bData4 = (Map<String, Object>) entry2.getValue(); //data
					// if(bData4.get("category_id").equals(map.get("category_id"))){  //如果配电室id相等
				map6= new HashMap<String, Object>();
				map6.put("category_branch_id", bData4.get("category_branch_id"));
				map6.put("category_id", bData4.get("category_id"));
				map6.put("category_name", bData4.get("category_name"));
				list2.add(map6);  //
			}}
		
		if(map.get("category_id")==null||map.get("category_id")==""){
			map.put("category_id",list2.get(0).get("category_id"));
		}
		List list3=new ArrayList(); 
		Map<String, Object> rmap= new HashMap<String, Object>();
		for(int i=0;i<list2.size();i++){
			if(list2.get(i).get("category_id").equals(map.get("category_id"))||list2.get(i).get("category_id")==map.get("category_id")){
				rmap.put("category_name", list2.get(i).get("category_name"));
				rmap.put("category_id", Integer.parseInt(list2.get(i).get("category_id").toString()));
				list3.add(list2.get(i).get("category_branch_id"));
			}
		}
		
		
		Map<String, Object> map4= new HashMap<String, Object>();   //接口数据
		map4.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map4.put("category_type",3);  //分类类别id
		map4.put("column_key",true);  //是否key值索引.
		map4.put("factory_id",map.get("factory_id"));  //工厂id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		String data = hc.get(GET_URL,map4); //接口数据String类型
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class);
		Map<String,Object> Data2=(Map<String, Object>) Data1.get("data");
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
		   Map<String,Object> Data3 = (Map<String, Object>) entry.getValue(); //data
						for(int i=0;i<list3.size();i++){
							if(list3.get(i).equals(Data3.get("category_id"))){
								 map3=new HashMap<String, Object>();
								 map3.put("branch_name",Data3.get("category_name")); 		//支路名称
							     map3.put("branch_id",Integer.parseInt(Data3.get("category_id").toString())); 		//支路ID
								 list.add( map3);
							}
						}
		}
		
		
		
		
		
		
			
			
		Map<String, Object> pmap= new HashMap<String, Object>();
		Map<String, Object> pmap2= new HashMap<String, Object>();
		List<Map<String, Object>> plist=new ArrayList<>(); 
		pmap.put("date_type", "days_data");
		pmap.put("date_from", map.get("start_date"));  //开始时间
		pmap.put("date_to", map.get("end_date"));		//结束时间
		
		for(int i=0;i<list.size();i++){
			pmap.put("meter_ids", list.get(i).get("branch_id"));
			pmap2=CEUtils.pmpEncrypt(pmap); //加密传输数据
			String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
			String pdata = hc.get(pGET_URL,pmap2); //接口数据String类型
			HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
			Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
			for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
				HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
				double a=0;
				for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
					HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
					a=a+Double.parseDouble(pData4.get("power").toString());
				}
				list.get(i).put("consumption_value", a);
			}
		
		}	
			
		int fana=0;
		int fanb=19;
		if(map.get("pageNum")==null||map.get("pageNum")==""||map.get("pageSize")==null||map.get("pageSize")==""){
			fana=0;
			fanb=19;
		}else{
			int pageNum=Integer.parseInt(map.get("pageNum").toString()); 
			int pageSize=Integer.parseInt(map.get("pageSize").toString());
			fana=(pageNum-1)*pageSize;
			fanb=pageNum*pageSize;
		}
		List<Map<String, Object>> fanlist=new ArrayList<>(); 
		
		map2.put("is_lastpage", false);
		for(int i=fana;i<fanb;i++){
			if(i>=list.size()){
				map2.put("is_lastpage", true);
				break;
			}
			fanlist.add(list.get(i));
		}
		map2.put("category_id",Integer.parseInt(rmap.get("category_id").toString()));
		map2.put("category_name", rmap.get("category_name"));
		map2.put("factory_name", fmap2.get("factory_name"));
		map2.put("factory_id", fmap2.get("factory_id"));
		map2.put("start_date", map.get("start_date"));
		map2.put("end_date", map.get("end_date"));
		map2.put("section_list", fanlist);  //单日列表
		
		return Result.success(map2);
	}
	
	
	/**
	 * 单日报表详情
	 */
	@Override
	public Object singleDay_detail(Map<String, Object> map) {
		if(map.get("factory_id")==null||map.get("factory_id")==""||map.get("branch_id")==null||map.get("branch_id")==""||map.get("date")==null||map.get("date")==""){
			return CodeMsg.MISSING_PARAMETER;
		}
		

		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		/**
		 * 查询单日报表
		 */
		Map<String, Object> map4= new HashMap<String, Object>();   //接口数据
		Map<String, Object> bmap=new HashMap<String, Object>();
		map4.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map4.put("category_type",3);  //分类类别id
		map4.put("column_key",true);  //是否key值索引.
		map4.put("factory_id",map.get("factory_id"));  //工厂id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		String data = hc.get(GET_URL,map4); //接口数据String类型
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class);
		Map<String,Object> Data2=(Map<String, Object>) Data1.get("data");
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
		   Map<String,Object> Data3 = (Map<String, Object>) entry.getValue(); //data
						if(map.get("branch_id").equals(Data3.get("category_id"))||Data3.get("category_id")==map.get("branch_id")){
							bmap.put("branch_name",Data3.get("category_name")); 		//支路名称
							bmap.put("branch_id",Integer.parseInt(Data3.get("category_id").toString())); 		//支路ID
						}
		}
		
		//报表数据	
		Map<String, Object> pmap= new HashMap<String, Object>();
		Map<String, Object> pmap2= new HashMap<String, Object>();
		List<Map<String, Object>> plist=new ArrayList<>(); 
		pmap.put("date_type", "day_data");
		pmap.put("date_from", map.get("date"));  //开始时间
		pmap.put("date_to", map.get("date"));		//结束时间
		pmap.put("meter_ids", map.get("branch_id"));
		pmap=CEUtils.pmpEncrypt(pmap); //加密传输数据
			String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
			String pdata = hc.get(pGET_URL,pmap); //接口数据String类型
			HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
			Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
			for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
				HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
				for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
					HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
					pmap2.put("consumption_value",Double.parseDouble(pData4.get("power").toString()));
					pmap2.put("power_per_hour", pData4.get("power_per_hour").toString());
				}
				
			}
			String power_per_hour=pmap2.get("power_per_hour").toString().replace("[", "");
			String power_per_hour2=power_per_hour.replace("]", "");
			List<String> list1 = Arrays.asList(power_per_hour2.split(","));
			for(int i=0;i<list1.size();i++){
				list1.get(i).trim();
				map3= new HashMap<String, Object>();
				map3.put("time",i+1);
				map3.put("power",Double.parseDouble(list1.get(i)));
				list.add(map3);
			}
			
			
		
		map2.put("branch_name", bmap.get("branch_name"));
		map2.put("branch_id", Integer.parseInt(map.get("branch_id").toString()));
		map2.put("factory_id",Integer.parseInt(map.get("factory_id").toString()));
		map2.put("date", map.get("date"));
		map2.put("total_power", pmap2.get("consumption_value"));
		map2.put("list", list);  //单日列表
		return Result.success(map2);
	}
	

	/**
	 * 区间报表详情
	 */
	@Override
	public Object section_detail(Map<String, Object> map) {
		if(map.get("factory_id")==null||map.get("factory_id")==""||map.get("branch_id")==null||map.get("branch_id")==""||map.get("start_date")==null||map.get("start_date")==""||map.get("end_date")==null||map.get("end_date")==""){
			return CodeMsg.MISSING_PARAMETER;
		}
		

		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		//支线数据
		Map<String, Object> map4= new HashMap<String, Object>();   //接口数据
		Map<String, Object> bmap=new HashMap<String, Object>();
		map4.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map4.put("category_type",3);  //分类类别id
		map4.put("column_key",true);  //是否key值索引.
		map4.put("factory_id",map.get("factory_id"));  //工厂id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		String data = hc.get(GET_URL,map4); //接口数据String类型
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class);
		Map<String,Object> Data2=(Map<String, Object>) Data1.get("data");
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
		   Map<String,Object> Data3 = (Map<String, Object>) entry.getValue(); //data
						if(map.get("branch_id").equals(Data3.get("category_id"))||Data3.get("category_id")==map.get("branch_id")){
							bmap.put("branch_name",Data3.get("category_name")); 		//支路名称
							bmap.put("branch_id",Data3.get("category_id")); 		//支路ID
						}
		}
		
		//报表数据	
		Map<String, Object> pmap= new HashMap<String, Object>();
		Map<String, Object> pmap2= new HashMap<String, Object>();
		List<Map<String, Object>> plist=new ArrayList<>(); 
		pmap.put("date_type", "days_data");
		pmap.put("date_from", map.get("start_date"));  //开始时间
		pmap.put("date_to", map.get("end_date"));		//结束时间
		pmap.put("meter_ids", map.get("branch_id"));
		pmap=CEUtils.pmpEncrypt(pmap); //加密传输数据
		double a=0;
			String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
			String pdata = hc.get(pGET_URL,pmap); //接口数据String类型
			HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
			Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
			for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
				HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
				for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
					HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
					pmap2= new HashMap<String, Object>();
					pmap2.put("date", pentry2.getKey());
					pmap2.put("power", Double.parseDouble(pData4.get("power").toString()));
					a=a+Double.parseDouble(pData4.get("power").toString());
					plist.add(pmap2);
				}
			}
		//排序
		ListSort.ListSort(plist);
		
		map2.put("branch_name", bmap.get("branch_name"));
		map2.put("branch_id", Integer.parseInt(map.get("branch_id").toString()));
		map2.put("factory_id", Integer.parseInt(map.get("factory_id").toString()));
		map2.put("start_date", map.get("start_date"));
		map2.put("end_date", map.get("end_date"));
		map2.put("list", plist);  //区间列表
		map2.put("total_power",a);  
		
		return Result.success(map2);
	}

	
	/**
	 * 进线分类单日
	 */
	@Override
	public Object singleDay_Coilin_sort(Map<String, Object> map) {
		if(map.get("factory_id")==null||map.get("factory_id")==""||map.get("branch_id")==null||map.get("branch_id")==""||map.get("date")==null||map.get("date")==""){
			return CodeMsg.MISSING_PARAMETER;
		}
		
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		
		
		//支线数据
		Map<String, Object> map4= new HashMap<String, Object>();   //接口数据
		Map<String, Object> bmap=new HashMap<String, Object>();
		Map<String, Object> bmap2=new HashMap<String, Object>();
		map4.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map4.put("category_type",3);  //分类类别id
		map4.put("column_key",true);  //是否key值索引.
		map4.put("factory_id",map.get("factory_id"));  //工厂id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		String data = hc.get(GET_URL,map4); //接口数据String类型
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class);
		Map<String,Object> Data2=(Map<String, Object>) Data1.get("data");
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
		   Map<String,Object> Data3 = (Map<String, Object>) entry.getValue(); //data
						if(map.get("branch_id").equals(Data3.get("parent_category_id"))||Data3.get("parent_category_id")==map.get("branch_id")){
							bmap=new HashMap<String, Object>();
							bmap.put("coilin_name",Data3.get("category_name")); 		//支路名称
							bmap.put("coilin_id",Integer.parseInt(Data3.get("category_id").toString())); 		//支路ID
							list.add(bmap);
						}
						
						if(map.get("branch_id").equals(Data3.get("category_id"))||Data3.get("category_id")==map.get("branch_id")){
							bmap2.put("branch_name", Data3.get("category_name"));
						}
		}
		
		
		
		
		Map<String, Object> pmap= new HashMap<String, Object>();
		Map<String, Object> pmap2= new HashMap<String, Object>();
		List<Map<String, Object>> plist=new ArrayList<>(); 
		pmap.put("date_type", "day_data");
		pmap.put("date_from", map.get("date"));
		pmap.put("date_to", map.get("date"));
		
		for(int i=0;i<list.size();i++){
			pmap.put("meter_ids", list.get(i).get("coilin_id"));
			pmap2=CEUtils.pmpEncrypt(pmap); //加密传输数据
			String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
			String pdata = hc.get(pGET_URL,pmap2); //接口数据String类型
			HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
			Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
			for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
				HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
				for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
					HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
					list.get(i).put("consumption_value",Double.parseDouble(pData4.get("power").toString()));
				}
			}
		
		}
		
		Map<String, Object> pmap3= new HashMap<String, Object>();
		double total_consumption_value=0;
		pmap3.put("date_type", "day_data");
		pmap3.put("date_from", map.get("date"));
		pmap3.put("date_to", map.get("date"));
		pmap3.put("meter_ids",map.get("branch_id"));
		pmap3=CEUtils.pmpEncrypt(pmap3); //加密传输数据
		String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
		String pdata = hc.get(pGET_URL,pmap3); //接口数据String类型
		HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
		Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
		for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
			HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
			for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
				HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
				total_consumption_value= Double.parseDouble(pData4.get("power").toString());
			}
		}
		
		
		int fana=0;
		int fanb=19;
		if(map.get("pageNum")==null||map.get("pageNum")==""||map.get("pageSize")==null||map.get("pageSize")==""){
			fana=0;
			fanb=19;
		}else{
			int pageNum=Integer.parseInt(map.get("pageNum").toString()); 
			int pageSize=Integer.parseInt(map.get("pageSize").toString());
			fana=(pageNum-1)*pageSize;
			fanb=pageNum*pageSize;
		}
		List<Map<String, Object>> fanlist=new ArrayList<>(); 
		
		map2.put("is_lastpage", false);
		for(int i=fana;i<fanb;i++){
			if(i>=list.size()){
				map2.put("is_lastpage", true);
				break;
			}
			fanlist.add(list.get(i));
		}
		
		
		map2.put("total_consumption_value", total_consumption_value);
		map2.put("branch_id", Integer.parseInt(map.get("branch_id").toString()));
		map2.put("factory_id", Integer.parseInt(map.get("factory_id").toString()));
		map2.put("branch_name", bmap2.get("branch_name"));
		map2.put("coilin_sort", fanlist);  //列表
		
		
		return Result.success(map2);
	}
	
	
	/**
	 * 进线分类区间
	 */
	@Override
	public Object section_Coilin_sort(Map<String, Object> map) {
		if(map.get("factory_id")==null||map.get("factory_id")==""||map.get("branch_id")==null||map.get("branch_id")==""||map.get("start_date")==null||map.get("start_date")==""||map.get("end_date")==null||map.get("end_date")==""){
			return CodeMsg.MISSING_PARAMETER;
		}
		
		
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		
		
		//支线数据
		Map<String, Object> map4= new HashMap<String, Object>();   //接口数据
		Map<String, Object> bmap=new HashMap<String, Object>();
		Map<String, Object> bmap2=new HashMap<String, Object>();
		map4.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map4.put("category_type",3);  //分类类别id
		map4.put("column_key",true);  //是否key值索引.
		map4.put("factory_id",map.get("factory_id"));  //工厂id
		map4=CEUtils.pmpEncrypt(map4); //加密传输数据
		String GET_URL = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		String data = hc.get(GET_URL,map4); //接口数据String类型
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class);
		Map<String,Object> Data2=(Map<String, Object>) Data1.get("data");
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
		   Map<String,Object> Data3 = (Map<String, Object>) entry.getValue(); //data
						if(map.get("branch_id").equals(Data3.get("parent_category_id"))||Data3.get("parent_category_id")==map.get("branch_id")){
							bmap=new HashMap<String, Object>();
							bmap.put("coilin_name",Data3.get("category_name")); 		//支路名称
							bmap.put("coilin_id",Integer.parseInt(Data3.get("category_id").toString())); 		//支路ID
							list.add(bmap);
						}
						
						if(map.get("branch_id").equals(Data3.get("category_id"))||Data3.get("category_id")==map.get("branch_id")){
							bmap2.put("branch_name", Data3.get("category_name"));
						}
		}
		
		
		
		
		Map<String, Object> pmap= new HashMap<String, Object>();
		Map<String, Object> pmap2= new HashMap<String, Object>();
		List<Map<String, Object>> plist=new ArrayList<>(); 
		pmap.put("date_type", "days_data");
		pmap.put("date_from", map.get("start_date"));
		pmap.put("date_to", map.get("end_date"));
		
		for(int i=0;i<list.size();i++){
			double a=0;
			pmap.put("meter_ids", list.get(i).get("coilin_id"));
			pmap2=CEUtils.pmpEncrypt(pmap); //加密传输数据
			String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
			String pdata = hc.get(pGET_URL,pmap2); //接口数据String类型
			HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
			Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
			for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
				HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
				for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
					HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
					a=a+Double.parseDouble(pData4.get("power").toString());
				}
				list.get(i).put("consumption_value",a);
			}
			
		}
		
		Map<String, Object> pmap3= new HashMap<String, Object>();
		pmap3.put("date_type", "days_data");
		pmap3.put("date_from", map.get("start_date"));
		pmap3.put("date_to", map.get("end_date"));
		pmap3.put("meter_ids",map.get("branch_id"));
		pmap3=CEUtils.pmpEncrypt(pmap3); //加密传输数据
		String pGET_URL = clientUrl.PHPCENTER+"elecreporting/get_data"; 
		String pdata = hc.get(pGET_URL,pmap3); //接口数据String类型
		HashMap<String,Object> pData = gson.fromJson(pdata, HashMap.class); //接口数据
		Map<String,Object> pData2 = gson.fromJson(pData.get("data").toString(), HashMap.class); //接口数据
		double b=0;
		for (Map.Entry<String, Object> pentry : pData2.entrySet()) {  //data
			HashMap<String,Object> pData3 = gson.fromJson(pentry.getValue().toString(), HashMap.class);
			for (Map.Entry<String, Object> pentry2 : pData3.entrySet()) {  //data
				HashMap<String,Object> pData4 = gson.fromJson(pentry2.getValue().toString(), HashMap.class);
				b=b+Double.parseDouble(pData4.get("power").toString());
			}
		}
		
		
		
		int fana=0;
		int fanb=19;
		if(map.get("pageNum")==null||map.get("pageNum")==""||map.get("pageSize")==null||map.get("pageSize")==""){
			fana=0;
			fanb=19;
		}else{
			int pageNum=Integer.parseInt(map.get("pageNum").toString()); 
			int pageSize=Integer.parseInt(map.get("pageSize").toString());
			fana=(pageNum-1)*pageSize;
			fanb=pageNum*pageSize;
		}
		List<Map<String, Object>> fanlist=new ArrayList<>(); 
		
		map2.put("is_lastpage", false);
		for(int i=fana;i<fanb;i++){
			if(i>=list.size()){
				map2.put("is_lastpage", true);
				break;
			}
			fanlist.add(list.get(i));
		}
		
		map2.put("total_consumption_value", b);
		map2.put("branch_id", Integer.parseInt(map.get("branch_id").toString()));
		map2.put("factory_id",Integer.parseInt(map.get("factory_id").toString()));
		map2.put("branch_name", bmap2.get("branch_name"));
		map2.put("coilin_sort", fanlist);  //列表
		
		return Result.success(map2);
	
	}
	
	
	
	
	
	
	
	/**
	 * 所选厂区电的区域分类列表
	 */
	@Override
	public Object get_distribution_room_list(Map<String, Object> map) {
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		Gson gson=new Gson();
		
		map.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
		map.remove("token");
		map.put("category_type",2);  //分类类别id
		map.put("column_key",true);  //分类类别id
		
		map=CEUtils.pmpEncrypt(map); //加密传输数据
		String GET_URL1 = clientUrl.PHPCENTER+"elecreporting/get_category_list";   //所选厂区电的区域分类列表
		HttpClientService hc1=new HttpClientService();
		String data1 = hc1.get(GET_URL1,map); //接口数据String类型
		HashMap<String,Object> factory_elecData = gson.fromJson(data1, HashMap.class);
		HashMap<String,Object> factory_elecData2 = gson.fromJson(factory_elecData.get("data").toString(), HashMap.class); //data
		for (Map.Entry<String, Object> entry : factory_elecData2.entrySet()) {  //data
			HashMap<String,Object> factory_elecData3 = gson.fromJson(entry.getValue().toString(), HashMap.class); //data的value
						map3=new HashMap<String, Object>();
					    map3.put("category_name",factory_elecData3.get("category_name")); 		//区域名称
						map3.put("category_id",factory_elecData3.get("category_id")); 		//区域ID
						map3.put("factory_id",factory_elecData3.get("factory_id")); 		//厂区ID
						list.add( map3);
		}
		return Result.success(list);
		
	}
	

	/**
	 * 分类列表
	 */
	@Override
	public Object get_category_list(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		Map<String, Object> map4= new HashMap<String, Object>();   //第一个接口传输数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		
		map.put("project_id", JwtToken.getProject_id(map.get("token")+"")); //项目id
		map.remove("token");  //去除token
		map.put("category_ids", 16);
		map=CEUtils.pmpEncrypt(map);
		//所有厂区电的区域分类所属电表列表
		String GET_URL = clientUrl.PHPCENTER+"synthesize_energy/get_factory_elec_category_meter_list";   //获取接口的数据
		HttpClientService hc=new HttpClientService();
		String data = hc.get(GET_URL,map); //接口数据String类型
		Gson gson=new Gson();
		HashMap<String,Object> Data1 = gson.fromJson(data, HashMap.class); //接口数据
//		JSONObject json = JSONObject.parseObject(data);  //转成json类型
//		JSONObject json2 =json.getJSONObject("data");
		System.out.println(Data1.get("data"));
		HashMap<String,Object> Data2 = gson.fromJson(Data1.get("data").toString(), HashMap.class); //接口数据
		for (Map.Entry<String, Object> entry : Data2.entrySet()) {  //data
			//JSONObject json3 = JSONObject.parseObject(entry.getValue().toString());  //转成json类型
			System.out.println(entry.getValue());
			HashMap<String,Object> Data3 = gson.fromJson(entry.getValue().toString(), HashMap.class); //data的value
			for (Map.Entry<String, Object> entry2 : Data3.entrySet()) { 
				HashMap<String,Object> Data4 = gson.fromJson(entry2.getValue().toString(), HashMap.class); //data的value的value
						map3=new HashMap<String, Object>();
					    map3.put("ammeterlist",Data4.get(map.get("category_ids"))); 		//电表列表
						list.add( map3);
			}
		}
		System.out.println(list);
		
		
		map2.put("code", 1);
		map2.put("msg", "OK");
		map2.put("data", list);
		return map2;
	}









	

	

	
	

	

}
