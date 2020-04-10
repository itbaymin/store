//定义模态框新增的方法
layui.define([ 'form', 'layer', 'jquery' ], function(exports) {
	var form = layui.form, 
	layer = layui.layer, 
	$ = layui.jquery;
	
	var obj = {
		show : function(title,url,w,h) {
			if (title == null || title == '') {
		        title=false;
		    };
		    if (url == null || url == '') {
		        url="404.html";
		    };
		    if (w == null || w == '') {
		        w=($(window).width()*0.9);
		    };
		    if (h == null || h == '') {
		        h=($(window).height() - 50);
		    };
		    layer.open({
		        type: 2,
		        area: [w+'px', h +'px'],
		        fix: false, //不固定
		        maxmin: true,
		        shadeClose: true,
		        shade:0.4,
		        title: title,
		        content: url
		    });
		},
		hide : function() {
			var index = parent.layer.getFrameIndex(window.name);
		    parent.layer.close(index);
		},
		modaladd : function(option) {
			var that = this;
			var options = {
				url : '',
				pre : function(){return true},
				filter : 'submit(add)',
				addition : {},//附加参数，为死数据
				succ : function(){},
				checkbox : []//当表单内有多个复选框的（相同name）使用该字段
			}
			Object.assign(options,option);
			// 监听提交
			form.on(options.filter, function(data) {
				if(!options.pre())
					return false;
				var lay_index = layer.load(); 
				var checkData = options.checkbox.length>0 && that.checkboxData(options.checkbox);
				Object.assign(data.field,options.addition,checkData);
				//请求后台
				$.ajaxSettings.traditional = true;
				$.post(options.url,data.field,function(result){
					layer.close(lay_index);
					if(result.code=='200'){
						options.succ();
						that.success(result.msg);
					}else if(result.code=='400'){
						that.fail(result.msg);
					}else{
						that.fail('服务器错误');
					}
				}).fail(function(){
					layer.close(lay_index);
					that.fail('请求出错');
				});
				return false;
			});
		},
		modaledit : function(option) {
			var that = this;
			var options = {
				url : '',
				pre : function(){return true},
				filter : 'submit(edit)',
				addition : {},
				succ : function(){},
				checkbox : []
			};
			Object.assign(options,option);
			// 监听提交
			form.on(options.filter, function(data) {
				if(!options.pre())
					return false;
				var lay_index = layer.load(); 
				var checkData = options.checkbox.length>0 && that.checkboxData(options.checkbox);
				Object.assign(data.field,options.addition,checkData);
				//请求后台
				$.ajaxSettings.traditional = true;
				
				$.post(options.url,data.field,function(result){
					layer.close(lay_index);
					if(result.code=='200'){
						options.succ();
						that.success(result.msg);
					}else if(result.code=='400'){
						that.fail(result.msg);
					}else{
						that.fail('服务器错误');
					}
				}).fail(function(){
					layer.close(lay_index);
					that.fail('请求出错');
				});
				return false;
			});
		},
		checkboxData : function(checks){
			var data = {};
			for(let check of checks){
				var res = new Array();
				$("input:checkbox[name='"+check+"']:checked").each(function() {
					res.push($(this).val());
	            });
				data[check] = res;
			}
			return data;
		},
		//成功通用方法
		success : function (msg){
			layer.alert(msg, {
				icon : 1
			}, function() {
				// 关闭弹框
				obj.hide();
			});
		},
		//失败方法
		fail : function (msg){
			layer.alert(msg, {
				icon : 2
			})
		}
	}
	exports('modal',obj);
})