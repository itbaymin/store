/*扩展请求*/
$.extend({
    req : function(options){
        //加载弹出层
        layui.use('layer',
            function() {
                var layer = layui.layer;
                var index = layer.load();
                var option = {
                    url : '',//url
                    async:true,
                    traditional:false,
                    data : '',//数据可为字符串和json
                    alert : true,//完成是否弹窗
                    succ : function(){},//操作返回200执行
                    fail : function(){},//操作返回400执行
                    error : function(){}//请求错误执行
                };
                Object.assign(option,options);
                $.ajaxSettings.async = option.async;
                $.ajaxSettings.traditional = option.traditional;
                $.post(option.url,option.data,function(data){
                    if(data.status=='200'){
                        if(!option.alert)
                            option.succ(data.data);
                        else{
                            layer.alert(data.message, {icon : 1});
                            option.succ(data.data);
                        }
                    }else{
                        option.fail(data);
                        option.alert && layer.alert(data.message, {icon : 2});
                    }
                    layer.close(index);
                }).fail(function(){
                    layer.close(index);
                    option.error();
                    layer.alert('请求失败', {
                        icon : 2
                    })
                });
                $.ajaxSettings.async = true;
                $.ajaxSettings.traditional = false;
            }
        )
    }
});
