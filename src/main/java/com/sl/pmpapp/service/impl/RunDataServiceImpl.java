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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sl.pmpapp.service.RunDataService;
import com.sl.pmpapp.utils.CEUtils;
import com.sl.pmpapp.utils.CodeMsg;
import com.sl.pmpapp.utils.HttpClientService;
import com.sl.pmpapp.utils.JedisPool;
import com.sl.pmpapp.utils.JwtToken;
import com.sl.pmpapp.utils.Result;
import com.sl.pmpapp.utils.RunFuns;
import redis.clients.jedis.Jedis;



@Service("runDataImpl")
public class RunDataServiceImpl implements RunDataService {
	
	@Autowired
	private JedisPool jedisPool;
	

	
	
	/**
	 * 获取进线列表
	 */
	@Override 
	public Object get_coilin_list(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		//厂区 判断 以及 返回.
				Map<String, Object> fmap= new HashMap<String, Object>();   //返回数据
				List<Map<String, Object>> flist=new ArrayList<>(); 
				fmap.put("project_id", JwtToken.getProject_id(map.get("token")+""));   //根据token获取project_id
				fmap=CEUtils.pmpEncrypt(fmap); //加密传输数据
				String fGET_URL = "http://192.168.0.60:8001/elecreporting/get_project_factorys";   //所有厂区
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
						if(flist.get(i).get("factory_id").toString().equals(map.get("factory_id").toString())||flist.get(i).get("factory_id")==map.get("factory_id")){
							fmap2.put("factory_name", flist.get(i).get("factory_name"));
					    	fmap2.put("factory_id", flist.get(i).get("factory_id"));
						}
					}				
				
				
				//配电室  判断  以及 返回
				List<Map<String, Object>> list2=new ArrayList<>();    //data 
				Map<String, Object> map5= new HashMap<String, Object>();   //接口数据
				Map<String, Object> map6= new HashMap<String, Object>();
				map5.put("project_id",JwtToken.getProject_id(map.get("token")+""));  //用户对应项目id 
				map5.put("column_key",true);  //是否key值索引.
				map5.put("factory_id",map.get("factory_id"));  //工厂id
				map5=CEUtils.pmpEncrypt(map5); //加密传输数据
				String GET_URL2 = "http://192.168.0.60:8001/elecreporting/get_transformerroom_list";   //所选厂区电的区域分类列表
				String data2 = hc.get(GET_URL2,map5); //接口数据String类型
				HashMap<String,Object> factory_elecData = gson.fromJson(data2, HashMap.class);
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
				if(map.get("rid")==null||map.get("rid")==""){
					map.put("rid",list2.get(0).get("rid"));
				}
				Map<String, Object> rmap= new HashMap<String, Object>();
				for(int i=0;i<list2.size();i++){
						if(list2.get(i).get("rid").toString().equals(map.get("rid").toString())||(int)list2.get(i).get("rid")==Integer.parseInt(map.get("rid").toString())){
							rmap.put("rname", list2.get(i).get("category_name"));
							rmap.put("rid",Integer.parseInt(list2.get(i).get("rid").toString()));
						}				
				}
				
				
				
				//进线列表数据
				List<Map<String, Object>> list3=new ArrayList<>();  
				Map<String, Object> map7= new HashMap<String, Object>(); 
				Map<String, Object> map8= new HashMap<String, Object>();
				map7.put("project_id", JwtToken.getProject_id(map.get("token")+""));
				map7.put("factory_id", map.get("factory_id"));
				String GET_URL3 = "http://192.168.0.60:8090/power/getElecRoomANdLineList";   //所选厂区电的区域分类列表
				String data3 = hc.get(GET_URL3,map7); //接口数据String类型
				JSONArray json = JSONArray.parseArray(data3); 
				
				for (int i = 0; i < json.size(); i++) { 
					Map<String,Object> coilData=json.getJSONObject(i);
					if(coilData.get("panel_id")==null||coilData.get("panel_id")==""){
						continue;
					}
					if(coilData.get("rid").toString().equals(map.get("rid").toString())||coilData.get("rid")==map.get("rid")){
						map8= new HashMap<String, Object>();
						map8.put("panel_id", coilData.get("panel_id"));
						map8.put("device_name", coilData.get("device_name"));
						map8.put("tg_id", coilData.get("tg_id"));
						map8.put("device_desc", coilData.get("device_desc"));
						list3.add(map8);
					}
				}
				
				//开关  ep
				for(int j=0;j<list3.size();j++){
					if(list3.get(j).get("tg_id")==null||list3.get(j).get("tg_id")==""){
						continue;
					}
					Map<String, Object> map9= new HashMap<String, Object>();
					map9.put("tg_name", list3.get(j).get("tg_id"));
					map9.put("device_name", list3.get(j).get("device_name"));
					map9.put("panel_id", list3.get(j).get("panel_id"));
					String GET_URL4 = "http://192.168.0.60:8090/power/getData";   //所选厂区电的区域分类列表
					String data4 = hc.get(GET_URL4,map9); //接口数据String类型
					HashMap<String,Object> deData = gson.fromJson(data4, HashMap.class);
					if(deData==null){
						list3.get(j).put("ep",-1);
						list3.get(j).put("di",-1);
						continue;
					}
					String ep=list3.get(j).get("device_name")+"_ep";
					String kgzt=list3.get(j).get("device_name")+"_kgzt";
					Map<String,Object> deData2=new HashMap<String, Object>();
					Map<String,Object> deData3=new HashMap<String, Object>();
					if(deData.get(ep)!=null){
						deData2 = (Map<String, Object>) deData.get(ep);
					}
					if(deData.get(kgzt)!=null){
						deData3 = (Map<String, Object>) deData.get(kgzt);
					}
					list3.get(j).put("ep",Double.parseDouble(deData2.get("val").toString()));
			    	double d3=Double.parseDouble(deData3.get("val").toString());
			    	Double D3=new Double(d3); 
			    	int i3=D3.intValue(); 
					list3.get(j).put("di",i3);
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
				
				for(int i=fana;i<fanb;i++){
					if(i>=list3.size()){
						break;
					}
					fanlist.add(list3.get(i));
				}
				if(fanlist.size()<Integer.parseInt(map.get("pageSize").toString())){
					map2.put("is_lastpage", true);
				}else{
					map2.put("is_lastpage", false);
				}
				map2.put("factory_id", fmap2.get("factory_id"));
				map2.put("factory_name", fmap2.get("factory_name"));
				map2.put("rid", rmap.get("rid"));
				map2.put("rname", rmap.get("rname"));
				map2.put("coilin_list",fanlist);
				
				return Result.success(map2);
	}

	

	
	/**
	 * 获取进线数据
	 */
	@Override
	public Object getCoilin_detail(Map<String, Object> map) {
		
		if(map.get("tg_id")==null||map.get("device_name")==null||map.get("panel_id")==null){
			return CodeMsg.MISSING_PARAMETER;
		}
		
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		
		Map<String, Object> map4= new HashMap<String, Object>();
		map4.put("tg_name",map.get("tg_id"));
		map4.put("device_name",map.get("device_name"));
		map4.put("panel_id",map.get("panel_id"));
		String GET_URL = "http://192.168.0.60:8090/power/getData";   //
		String data = hc.get(GET_URL,map4); //接口数据String类型
		if(data==null||data.equals("")){
			return CodeMsg.NOT_FIND_DATA;
		}
		HashMap<String,Object> deData = gson.fromJson(data, HashMap.class);
		String a=map.get("device_name").toString();
		
		Map<String,Object> deData2=new HashMap<String, Object>();
		String b;
		String c;
		String d;
		b=((Map<String, Object>)deData.get(a+"_kgzt")).get("val").toString();
    	double d1=Double.parseDouble(b);
    	Double D1=new Double(d1); 
    	int i1=D1.intValue(); 
		deData2.put("name", "Di");
		deData2.put("value", i1+"");
		list.add(deData2);
		if(b.contains("1")){
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_ua"))==null?"0":((Map<String, Object>)deData.get(a+"_ua")).get("val").toString();
		c=((Map<String, Object>)deData.get(a+"_ub"))==null?"0":((Map<String, Object>)deData.get(a+"_ub")).get("val").toString();
		d=((Map<String, Object>)deData.get(a+"_uc"))==null?"0":((Map<String, Object>)deData.get(a+"_uc")).get("val").toString();
		deData2.put("name", "Ua/Ub/Uc (V)");
		deData2.put("value", b+" / "+c+" / "+d);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_ia"))==null?"0":((Map<String, Object>)deData.get(a+"_ia")).get("val").toString();
		c=((Map<String, Object>)deData.get(a+"_ib"))==null?"0":((Map<String, Object>)deData.get(a+"_ib")).get("val").toString();
		d=((Map<String, Object>)deData.get(a+"_ic"))==null?"0":((Map<String, Object>)deData.get(a+"_ic")).get("val").toString();
		deData2.put("name", "Ia/Ib/Ic (A)");
		deData2.put("value", b+" / "+c+" / "+d);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_pa"))==null?"0":((Map<String, Object>)deData.get(a+"_pa")).get("val").toString();
		c=((Map<String, Object>)deData.get(a+"_pb"))==null?"0":((Map<String, Object>)deData.get(a+"_pb")).get("val").toString();
		d=((Map<String, Object>)deData.get(a+"_pc"))==null?"0":((Map<String, Object>)deData.get(a+"_pc")).get("val").toString();
		deData2.put("name", "Pa/Pb/Pc (KW)");
		deData2.put("value", b+" / "+c+" / "+d);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_qa"))==null?"0":((Map<String, Object>)deData.get(a+"_qa")).get("val").toString();
		c=((Map<String, Object>)deData.get(a+"_qb"))==null?"0":((Map<String, Object>)deData.get(a+"_qb")).get("val").toString();
		d=((Map<String, Object>)deData.get(a+"_qc"))==null?"0":((Map<String, Object>)deData.get(a+"_qc")).get("val").toString();
		deData2.put("name", "Qa/Qb/Qc (kVar)");
		deData2.put("value", b+" / "+c+" / "+d);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_ep"))==null?"0":((Map<String, Object>)deData.get(a+"_ep")).get("val").toString();
		deData2.put("name", "Ep (kWh)");
		deData2.put("value", b);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_eq"))==null?"0":((Map<String, Object>)deData.get(a+"_eq")).get("val").toString();
		deData2.put("name", "Eq (kVarh)");
		deData2.put("value", b);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_cosq"))==null?"0":((Map<String, Object>)deData.get(a+"_cosq")).get("val").toString();
		deData2.put("name", "COSφ");
		deData2.put("value", b);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_g1_thd-ia"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-ia")).get("val").toString();
		c=((Map<String, Object>)deData.get(a+"_g1_thd-ib"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-ib")).get("val").toString();
		d=((Map<String, Object>)deData.get(a+"_g1_thd-ic"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-ic")).get("val").toString();
		deData2.put("name", "THD(Ia)/THD(Ib)/THD(Ic)");
		deData2.put("value", b+" / "+c+" / "+d);
		list.add(deData2);
		
		deData2=new HashMap<String, Object>();
		b=((Map<String, Object>)deData.get(a+"_g1_thd-va"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-va")).get("val").toString();
		c=((Map<String, Object>)deData.get(a+"_g1_thd-vb"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-vb")).get("val").toString();
		d=((Map<String, Object>)deData.get(a+"_g1_thd-vc"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-vc")).get("val").toString();
		deData2.put("name", "THD(Va)/THD(Vb)/THD(Vc)");
		deData2.put("value", b+" / "+c+" / "+d);
		list.add(deData2);
		}else{
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Ua/Ub/Uc (V)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Ia/Ib/Ic (A)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Pa/Pb/Pc (KW)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Qa/Qb/Qc (kVar)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Ep (kWh)");
			deData2.put("value", "0");
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Eq (kVarh)");
			deData2.put("value", "0");
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "COSφ");
			deData2.put("value", "0");
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "THD(Ia)/THD(Ib)/THD(Ic)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "THD(Va)/THD(Vb)/THD(Vc)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
		}
		
		ArrayList<Map<String,Object>> outLineData=(ArrayList<Map<String, Object>>) deData.get("outLineList");
		List<Map<String, Object>> list2=new ArrayList<>();    //data 多条数据
		for(int i=0;i<outLineData.size();i++){
			Map<String, Object> map5= new HashMap<String, Object>();
	    	double d2=Double.parseDouble(outLineData.get(i).get("panel_id").toString());
	    	Double D2=new Double(d2); 
	    	int i2=D2.intValue(); 
	    	map5.put("panel_id", i2);
			map5.put("device_name", outLineData.get(i).get("device_name"));
			map5.put("tg_id", outLineData.get(i).get("tg_id"));
			map5.put("device_desc", outLineData.get(i).get("device_desc"));
			list2.add(map5);
		}
		
		
		
		for(int j=0;j<list2.size();j++){
			if(list2.get(j).get("tg_id")==null||list2.get(j).get("tg_id")==""){
				continue;
			}
			Map<String, Object> map9= new HashMap<String, Object>();
			map9.put("tg_name", list2.get(j).get("tg_id"));
			map9.put("device_name", list2.get(j).get("device_name"));
			map9.put("panel_id", list2.get(j).get("panel_id"));
			String GET_URL4 = "http://192.168.0.60:8090/power/getData";   //所选厂区电的区域分类列表
			String data4 = hc.get(GET_URL4,map9); //接口数据String类型
			HashMap<String,Object> outdeData = gson.fromJson(data4, HashMap.class);
			if(deData==null){
				list2.get(j).put("ep",-1);
				list2.get(j).put("di",-1);
				continue;
			}
			String ep=list2.get(j).get("device_name")+"_ep";
			String kgzt=list2.get(j).get("device_name")+"_kgzt";
			Map<String,Object> outdeData2=new HashMap<String, Object>();
			Map<String,Object> outdeData3=new HashMap<String, Object>();
			if(outdeData.get(ep)!=null){
				outdeData2 = (Map<String, Object>) outdeData.get(ep);
			}
			if(outdeData.get(kgzt)!=null){
				outdeData3 = (Map<String, Object>) outdeData.get(kgzt);
			}
			list2.get(j).put("coilout_ep",Double.parseDouble(outdeData2.get("val").toString()));
	    	double d3=Double.parseDouble(outdeData3.get("val").toString());
	    	Double D3=new Double(d3); 
	    	int i3=D3.intValue(); 
	    	list2.get(j).put("di",i3);
		}
		
		Map<String,Object> fanmap2=new HashMap<String, Object>();
		fanmap2.put("details_list",list);
		fanmap2.put("coilout_list",list2);
		return Result.success(fanmap2);
	}

	
	/**
	 * 出线数据
	 */
	@Override
	public Object getCoilout_detail(Map<String, Object> map) {
		if(map.get("tg_id")==null||map.get("device_name")==null||map.get("panel_id")==null){
			return CodeMsg.MISSING_PARAMETER;
		}
		
		Map<String, Object> map3= new HashMap<String, Object>();   //一条数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		Gson gson=new Gson();
		
		
		Map<String, Object> map4= new HashMap<String, Object>();
		map4.put("tg_name",map.get("tg_id"));
		map4.put("device_name",map.get("device_name"));
		map4.put("panel_id",map.get("panel_id"));
		String GET_URL = "http://192.168.0.60:8090/power/getData";   //
		String data = hc.get(GET_URL,map4); //接口数据String类型
		if(data==null||data.equals("")){
			return CodeMsg.NOT_FIND_DATA;
		}
		HashMap<String,Object> deData = gson.fromJson(data, HashMap.class);
		String a=map.get("device_name").toString();
		
		Map<String,Object> deData2=new HashMap<String, Object>();
		String b;
		String c;
		String d;
		b=((Map<String, Object>)deData.get(a+"_kgzt")).get("val").toString();
    	double d1=Double.parseDouble(b);
    	Double D1=new Double(d1); 
    	int i1=D1.intValue(); 
		deData2.put("name", "Di");
		deData2.put("value", i1+"");
		list.add(deData2);
		if(b.contains("1")){
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_ua"))==null?"0":((Map<String, Object>)deData.get(a+"_ua")).get("val").toString();
			c=((Map<String, Object>)deData.get(a+"_ub"))==null?"0":((Map<String, Object>)deData.get(a+"_ub")).get("val").toString();
			d=((Map<String, Object>)deData.get(a+"_uc"))==null?"0":((Map<String, Object>)deData.get(a+"_uc")).get("val").toString();
			deData2.put("name", "Ua/Ub/Uc (V)");
			deData2.put("value", b+" / "+c+" / "+d);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_ia"))==null?"0":((Map<String, Object>)deData.get(a+"_ia")).get("val").toString();
			c=((Map<String, Object>)deData.get(a+"_ib"))==null?"0":((Map<String, Object>)deData.get(a+"_ib")).get("val").toString();
			d=((Map<String, Object>)deData.get(a+"_ic"))==null?"0":((Map<String, Object>)deData.get(a+"_ic")).get("val").toString();
			deData2.put("name", "Ia/Ib/Ic (A)");
			deData2.put("value", b+" / "+c+" / "+d);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_pa"))==null?"0":((Map<String, Object>)deData.get(a+"_pa")).get("val").toString();
			c=((Map<String, Object>)deData.get(a+"_pb"))==null?"0":((Map<String, Object>)deData.get(a+"_pb")).get("val").toString();
			d=((Map<String, Object>)deData.get(a+"_pc"))==null?"0":((Map<String, Object>)deData.get(a+"_pc")).get("val").toString();
			deData2.put("name", "Pa/Pb/Pc (KW)");
			deData2.put("value", b+" / "+c+" / "+d);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_qa"))==null?"0":((Map<String, Object>)deData.get(a+"_qa")).get("val").toString();
			c=((Map<String, Object>)deData.get(a+"_qb"))==null?"0":((Map<String, Object>)deData.get(a+"_qb")).get("val").toString();
			d=((Map<String, Object>)deData.get(a+"_qc"))==null?"0":((Map<String, Object>)deData.get(a+"_qc")).get("val").toString();
			deData2.put("name", "Qa/Qb/Qc (kVar)");
			deData2.put("value", b+" / "+c+" / "+d);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_ep"))==null?"0":((Map<String, Object>)deData.get(a+"_ep")).get("val").toString();
			deData2.put("name", "Ep (kWh)");
			deData2.put("value", b);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_eq"))==null?"0":((Map<String, Object>)deData.get(a+"_eq")).get("val").toString();
			deData2.put("name", "Eq (kVarh)");
			deData2.put("value", b);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_cosq"))==null?"0":((Map<String, Object>)deData.get(a+"_cosq")).get("val").toString();
			deData2.put("name", "COSφ");
			deData2.put("value", b);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_g1_thd-ia"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-ia")).get("val").toString();
			c=((Map<String, Object>)deData.get(a+"_g1_thd-ib"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-ib")).get("val").toString();
			d=((Map<String, Object>)deData.get(a+"_g1_thd-ic"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-ic")).get("val").toString();
			deData2.put("name", "THD(Ia)/THD(Ib)/THD(Ic)");
			deData2.put("value", b+" / "+c+" / "+d);
			list.add(deData2);
			
			deData2=new HashMap<String, Object>();
			b=((Map<String, Object>)deData.get(a+"_g1_thd-va"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-va")).get("val").toString();
			c=((Map<String, Object>)deData.get(a+"_g1_thd-vb"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-vb")).get("val").toString();
			d=((Map<String, Object>)deData.get(a+"_g1_thd-vc"))==null?"0":((Map<String, Object>)deData.get(a+"_g1_thd-vc")).get("val").toString();
			deData2.put("name", "THD(Va)/THD(Vb)/THD(Vc)");
			deData2.put("value", b+" / "+c+" / "+d);
			list.add(deData2);
		}else{
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Ua/Ub/Uc (V)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Ia/Ib/Ic (A)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Pa/Pb/Pc (KW)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Qa/Qb/Qc (kVar)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Ep (kWh)");
			deData2.put("value", "0");
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "Eq (kVarh)");
			deData2.put("value", "0");
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "COSφ");
			deData2.put("value", "0");
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "THD(Ia)/THD(Ib)/THD(Ic)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
			deData2=new HashMap<String, Object>();
			deData2.put("name", "THD(Va)/THD(Vb)/THD(Vc)");
			deData2.put("value", 0+" / "+0+" / "+0);
			list.add(deData2);
		}
		return Result.success(list);
	}
	
	
	/**
	 * 开关接口
	 */
	@Override
	public Object set_di(Map<String, Object> map) {
		if(map.get("tg_id")==null||map.get("device_name")==null||map.get("factory_id")==null||map.get("val")==null||map.get("opassword")==null){
			return CodeMsg.MISSING_PARAMETER;
		}
		
		Gson gson=new Gson();
		HashMap<String,Object> userData = gson.fromJson(JwtToken.getAppUID(map.get("token")+""), HashMap.class);
		if(userData.get("oper_pwd").equals(map.get("opassword"))){
		Map<String, Object> map2= new HashMap<String, Object>();   //返回数据
		Map<String, Object> map3= new HashMap<String, Object>();   //接口发送数据
		List<Map<String, Object>> list=new ArrayList<>();    //data 多条数据
		HttpClientService hc=new HttpClientService();
		map3.put("project_id",JwtToken.getProject_id(map.get("token")+""));
		map3.put("tg_name",map.get("tg_id"));
		map3.put("tag", map.get("device_name"));
		map3.put("factory_id", map.get("factory_id"));
		if(map.get("val").equals(1)||map.get("val")=="1"||Integer.parseInt(map.get("val").toString())==1){
			map3.put("val", 0);
		}else{
			map3.put("val", 1);
		}
		String GET_URL ="http://192.168.0.60:8090/power/postForObject";
		String data = hc.get(GET_URL,map3); //接口数据String类型
		HashMap<String,Object> diData = gson.fromJson(data, HashMap.class); //接口数据
		if(diData.get("message").equals("操作成功")){
			return CodeMsg.SUCCESS;
		}else{
			return CodeMsg.OPERATION_FAIL;
		}
		}else{
			return CodeMsg.PW_INCORRECT;
		}
		
		
		
	}
	


	/**
	 * 开关日志
	 */
	@Override
	public Object di_Log(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}



	/**
	 * 分类列表
	 */
	@Override
	public Object get_category_list(Map<String, Object> map) {
		
		return null;
	}



}
