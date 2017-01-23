$(document).ready(function(){
	jQuery.ajaxSetup({
		beforeSend:function(e){
			var _that=this;
			var _url=_that.url;
			if(_url.indexOf("springraintoken")!=-1)return;
			if(_url.indexOf("?")!=-1){
				_that.url= _url+"&springraintoken="+springraintoken;
			}else{
				_that.url= _url+"?springraintoken="+springraintoken;
			}
		}
	});
	//初始化插件
	configLayui("global");
	//加载菜单
	loadMenu();
	init_sort_btn();
	init_button_action();
});
function loadMenu(){
	//加载菜单
    if(!(!!locache.get("menuData"))){//没有数据
    	ajaxmenu();
    }else{
    	var menuData = locache.get("menuData");
    	buildModule(menuData);
    }
}




function exit(){
	springrain.confirm("确定退出？", function(){
		try{
			locache.flush();
		}catch(e){}
		window.location = ctx+"/system/logout";
	});
	
}

function configLayui(par){
	layui.config({
		  base: ctx+"/layui/lay/modules/"
		}).use(par);
}



/**
 * 获取所有导航资源
 */
function ajaxmenu() {
    jQuery.ajax({
        url : ctx + "/system/menu/leftMenu",
        type : "post",
        data:{"springraintoken":springraintoken},
        cache : false,
        async : false,
        scriptCharset : "utf-8",
        dataType : "json",
        success : function(_json) {
            if(_json.status=="success"){
            	locache.set("menuData",_json.data);
            	var menuData = locache.get("menuData");
                buildModule(menuData);
            }
        }
    });
}

function buildModule(data) {
    if (data != null && typeof (data) != "undefined") {
        var htmlStr = '';
        /*处理/update时丢了菜单 状态*/
        var _url=window.location.pathname;
        if(_url.indexOf('/update/pre')!=-1){
        	_url=_url.substring(0,_url.indexOf("/update/pre"))+"/list";
        } 
        /*处理/update时丢了菜单 状态*/
        var childrenMenuList = null;
        for ( var i = 0; i < data.length; i++) {
            var url = data[i].pageurl;
            var tmpData = data[i]['leaf'][0];
            while(!!tmpData){
                url = tmpData.pageurl;
                tmpData = tmpData['leaf'][0];
            }
            var tmpPid = locache.get("currentPagePid");
            if((tmpPid == data[i].id) || (!(!!tmpPid) && i==0)){//url中有第一个菜单的键值
            	url = ctx + url;
            	htmlStr += '<li id="pmenu'+data[i].id+'" class="layui-nav-item layui-this"><a data-pid="'+data[i].id+'" data-action="'+url+'">'+data[i].name+'</a></li>';
                childrenMenuList = data[i]['leaf'];
            }else{
            	url = ctx + url;
                htmlStr += '<li id="pmenu'+data[i].id+'" class="layui-nav-item"><a data-pid="'+data[i].id+'" data-action="'+url+'">'+data[i].name+'</a></li>';
            }
        }
        $("#naviHeaderMenu").html(htmlStr);
        $("#menu").html(getParentModule(childrenMenuList));
    }
}


function getParentModule(childrenMenuList) {
    var htmlStr = '';
    var _url=window.location.pathname;
    if(_url.indexOf('/update/pre')!=-1){
    	_url=_url.substring(0,_url.indexOf("/update/pre"))+"/list";
    } 
    for(var i=0;i<childrenMenuList.length;i++){
    	 var showItem = "";
        if((ctx+childrenMenuList[i].pageurl) ==_url){
            showItem = "layui-this";
        }
       
        var _leaf=childrenMenuList[i]["leaf"];
        var menuIcon_df="&#xe63c;";
        if(childrenMenuList[i].menuIcon!=null){
        	menuIcon_df=childrenMenuList[i].menuIcon;
        }
        if(_leaf&&_leaf.length>0){
        	 htmlStr += '<li class="layui-nav-item '+showItem+'" id="'+childrenMenuList[i].id+'"><a href="';
            htmlStr = htmlStr+ ' javascript:;"> <i class="layui-icon">'+menuIcon_df+'</i><cute>'+childrenMenuList[i].name+'</cute></a>';
            htmlStr = htmlStr+getChindModule(_leaf);
        }else{
        	htmlStr += '<li class="layui-nav-item '+showItem+'" id="'+childrenMenuList[i].id+'"><a data-pid="'+childrenMenuList[i].pid+'" data-action="';
        	 var url = childrenMenuList[i].pageurl;
             var tmpData = childrenMenuList[i]['leaf'][0];
             while(!!tmpData){
                 url = tmpData.pageurl;
                 tmpData = tmpData['leaf'][0];
             }
             url = ctx+url;
            htmlStr = htmlStr+url+'" ><i class="layui-icon">'+menuIcon_df+'</i><cite>'+childrenMenuList[i].name+'</cite></a>';
        }
        htmlStr = htmlStr+'</li>';
    }
    return htmlStr;
}

function getChindModule(_leaf) {
    var showItem = "";
    var t = '<dl class="layui-nav-child">';
    for ( var menuObj in _leaf) {
        if((ctx+_leaf[menuObj].pageurl)==window.location.pathname){
            showItem = "layui-this";
        }
        t = t+'<dd class="'+showItem+'" pageUrl="'+_leaf[menuObj].pageurl+'" id="'+_leaf[menuObj].id+'"><a href="javascript:;"><i class="layui-icon">'+_leaf[menuObj].menuIcon+'</i><cite>'+_leaf[menuObj].name+'</cite></a></dd>';
    }
    t = t+'</dl>';
    return t;
}


/**
 * 获取url中的hash值，例如http://www.baidu.com#keyword,返回['keyword']
 * @returns 数组
 */
function getLocationHashArr(){
    var urlHash = window.location.hash;
    if(!!urlHash){
        var urlHashArr = urlHash.split("#");
        urlHashArr.splice(0,1);
        return urlHashArr;
    }
    return new Array();
}
function gentimestampstr(){
	return new Date().getTime();
}
function getcurrentMenuId(){
	var currentPageUrl=window.location.href;
	var urlElementArr=currentPageUrl.split("?");
	var menuId='';
	if(urlElementArr.length>1){//非首页
		var paramElementArr=urlElementArr[1].split("#");
		menuId= paramElementArr[0].split("=")[1];
		menuId=menuId.replace("&t","");
	}
	return menuId;
}
 

/**
 * 批量删除
 * @param _url
 * @param formId
 * @param listage
 * @returns {Boolean}
 */
function mydeletemore(_url, formId,listage) {
	var arr = new Array();
	var checks = jQuery(":checkbox[name='check_li']");
	checks.each(function(i, _obj) {
		if (_obj.checked) {
			arr.push(_obj.value);
		}

	});
	if (arr.length < 1) {
		myalert("请选择要删除的记录!");
		return false;
	}

	myconfirm("确定删除选中数据?", function() {
		myhref2page(_url,listage,"records=" + arr.join(","));
	});
}




function myexport(formId, _url) {
	var _form = document.getElementById(formId);
	var _action = _form.action;
	_form.action = _url;
	_form.submit();
	_form.action = _action;
}
/* 赋值 */
function set_val(name, val) {
	if ($("#" + name + " option").length > 0) {
		//按老的UI不动是这个
		//$("#" + name).val(val);
		//按新的layerui只能，模拟点击
		jQuery("#"+name).siblings("div").filter(".layui-form-select").eq(0).find("dd[lay-value='"+val+"']").trigger("click");
		return;
	}

	if (($("#" + name).attr("type")) === "checkbox") {
		if (val == 1) {
			$("#" + name).attr("checked", true);
			return;
		}
	}
	if ($("." + name).length > 0) {
		if (($("." + name).first().attr("type")) === "checkbox") {
			var arr_val = val.split(",");
			for ( var s in arr_val) {
				$("input." + name + "[value=" + arr_val[s] + "]").attr(
						"checked", true);
			}
		}
	}
	if (($("#" + name).attr("type")) === "text") {
		if(typeof val==="number"&&val.length>11){
			try{
				val=getSmpFormatDateByLong(val);
				$("#" + name).val(val);
				return;
			}catch(e){
				$("#" + name).val(val);
				return;
			}
		}
		$("#" + name).val(val);
		return;
	}
	if (($("#" + name).attr("type")) === "hidden") {
		$("#" + name).val(val);
		return;
	}
	if (($("#" + name).attr("rows")) > 0) {
		$("#" + name).val(val);
		return;
	}
}
function  init_sort_btn(){
	//加载颜色
	var _sort=jQuery("#page_sort").val();
	var _order=jQuery("#page_order").val();
	if(_order){
		if("asc"==_sort){
			jQuery("#th_"+_order).find(".sort-icon-up").css("color","#333333");
		}else{
			jQuery("#th_"+_order).find(".sort-icon-down").css("color","#333333");
		}
	}
	
	jQuery(".sort-icon").bind("mouseenter",function(){
		jQuery(this).addClass("sort-icon-on");
	}).bind("mouseout",function(){
		jQuery(this).removeClass("sort-icon-on");
	});
	//单击事件
	jQuery(".sort-icon").bind("click",function(){
		if(jQuery(this).hasClass("sort-icon-down")){
			jQuery("#page_sort").val("desc");
			jQuery("#page_order").val(jQuery(this).parent("th").attr("id").split("_")[1]);
		}else{
			jQuery("#page_sort").val("asc");
			jQuery("#page_order").val(jQuery(this).parent("th").attr("id").split("_")[1]);
		}
		//提交表单
		springrain.commonSubmit("searchForm");
		//$("#searchForm").submit();
	});
}
function init_button_action(){
	jQuery("button[data-action]").bind("click",function(){
		if(!!$(this).attr("data-pid"))
			locache.set("currentPagePid",$(this).attr("data-pid"));
		window.location.href=springrain.appendToken(jQuery(this).attr("data-action"));
	});
	jQuery("a[data-action]").bind("click",function(){
		if(!!$(this).attr("data-pid"))
			locache.set("currentPagePid",$(this).attr("data-pid"));
		window.location.href=springrain.appendToken(jQuery(this).attr("data-action"))
	});
}


