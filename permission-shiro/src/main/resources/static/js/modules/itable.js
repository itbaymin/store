//定义表格操作
layui.define(['table','layer','jquery','form'], function(exports) {
	var table=layui.table, layer=layui.layer, $=layui.jquery, form=layui.form, excel=layui.excel;
	var $cols,$count,$url;
	
	var obj = {
		render : function(option){
			var options = {
				heigth : 500,
				//toolbar: true,
                page : { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
                    layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局
                    curr: 1, //设定初始在第 5 页
                    groups: 3, //只显示 1 个连续页码
                    first: true,
                    last: true
                },
				limit : 10,
				limits : [10,15,30,50],
				method : 'post',
				even : true,
				autoSort : false,
			    text: {
				    none: '暂无相关数据' 
			    },
				parseData : function(res) {
					$count = res.data.count;
					return {
						'code' : res.status,
						'msg' : res.message,
						'data' : res.data.list,
						'count' : res.data.count
					}
				},
				response : {
					statusCode : 200
				}
			};
			var colappend = {
				align : 'center'
			};
			if(option.cols) {
				for(let col of option.cols[0]){
					Object.assign(col,colappend);
				}
			}
			Object.assign(options,option);
			options.id = options.elem;
			table.render(options);
			//赋值为数据导出准备
			$cols = option.cols[0];
			$url = option.url;
		},
		sort : function(id,filter){
			//监听排序事件 
			table.on('sort('+filter+')', function(obj){ 
			    table.reload(id, {
				    initSort: obj,
				    where: { 
				      field: obj.field,
				      sort: obj.type 
				    }
			    });
			});
		},
		search : function(tableId){
			var filter = arguments[1] || 'search';
			form.on('submit('+filter+')',function(data){
   				table.reload(tableId,{
   					page: {
                        layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局
                        curr: 1, //设定初始在第 5 页
                        groups: 3, //只显示 1 个连续页码
                        first: true,
                        last: true
   			        },
       				where: data.field
       			})
   	   			return false;
   			})
		},
		reload : function(tableId,where){
			if(where){
                where.page = {
                    layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局
                    curr: 1, //设定初始在第 5 页
                    groups: 3, //只显示 1 个连续页码
                    first: true,
                    last: true
				};
			}else{
				where = {};
			}
			table.reload(tableId,where);
		},
		toolBar : function(tableId,funcs){
			var common = {
				checkData : function(){
					return table.checkStatus(tableId).data;
				}
			}
			Object.assign(funcs,common);
			$('xblock .layui-btn').on('click', function(){
		    	var type = $(this).data('type');
		    	var check = $(this).data('check');
		    	if(check=='one' && funcs.checkData().length!=1){
		    		layer.msg("请选择一条数据");
		    		return;
		    	}else if(check=='more' && funcs.checkData().length<1){
		    		layer.msg("请至少选择一条数据");
		    		return;
		    	}
		    	if(type=='deleteSelected'){
		    		layer.open({
    					title:'提示',
    					content:'确认要删除所选项么？',
    					area: ['250px', '150px'],
    					btn: ['确定','取消'],
    					yes:function(index){
    						layer.close(index);
    						funcs[type] ? funcs[type].call(funcs,funcs.checkData()) : '';
    					}
   					});
		    	}else if(type=='export'){
		    		var query = {},title = [],obj={},xls = [[]];
		    		query.page = 1;
		    		query.limit = $count;
		    		for(let col of $cols){
		    			if(col.field=='opt')
		    				continue;
   						col.title && (obj[col.title]=[col.field,col.templet || '']);
   						col.title && title.push(col.title);
   					}
		    		if(funcs[type]){//文件名不包含后缀名
		    			var fileName = funcs[type].call(funcs,title,obj,query);
		    			xls.push(title);
		    			$.req({
	   						url:$url,
	   						alert:false,
	   						data:query,
	   						succ:function(json){
	   							if(json==null || json.list.length==0){
	   								layer.alert('无导出数据');
	   								return;
	   							}
	   							for(let row of json.list){
	   								xls.push($dealRow(obj,row));
	   							}
	   							layui.use('excel',function(){
	   								var excel = layui.excel;
	   								excel.exportExcel({
		   								sheet1: xls
		   							}, fileName+'.xlsx', 'xlsx');
	   							})
	   						}
	   					})
		    		}else{
		    			layer.alert('程序出错');
		    		}
		    	}else{
		    		funcs[type] ? funcs[type].call(funcs,funcs.checkData()) : '';
		    	}
		  	});
		}
	}
	exports('itable',obj);
})