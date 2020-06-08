var socket;
new Vue({
    el: '#app',
    data: {
        remind:{show:false,message:''},
        message: '',
        groupflag:'0',
        anistyle:'',            //切换动画样式类
        tab:'tab1',             //当前页面类型
        sub_tab:'friend',       //当前选项卡
        chat_type:'',           //聊天类型
        chat_id: 0,             //聊天对象id
        curr_friend:{},
        curr_records:[],
        curr_user:_user,
        TopImg:[
            {
                src:"/image/headimg1.jpg",
                link:"http://www.fanze.online",
            },{
                src:"/image/headimg2.jpg",
                link:"http://fanze.online",
            }
        ],
        //群聊列表
        grolists:[{
            headImg:"/image/headimg2.jpg",
            uid:'00002'
        }]
    },
    methods: {
        buildbg:function (src) {
            return "background-image:url('"+src+"')";
        },
        checkFlag:function(groupflag){
            if(this.groupflag.search(groupflag)!=-1)
                return true;
            else
                return false;
        },
        enableSearch:function () {
            $('.ui-searchbar').tap(function(){
                $('.ui-searchbar-wrap').addClass('focus');
                $('.ui-searchbar-input input').focus();
            });
            $('.ui-searchbar-cancel').tap(function(){
                $('.ui-searchbar-wrap').removeClass('focus');
            });
        },
        //tab跳转
        goToTab: function(tab) {
            if(this.tab > tab)
                this.anistyle='ltor';
            else
                this.anistyle='rtol';
            this.tab=tab;
            document.documentElement.scrollTop=0;
            document.body.scrollTop=0;
            this.enableSearch();
        },
        //获取朋友信息
        getFriend:function (id) {
            for (let i in friends){
                if(friends[i].id==id)
                    return friends[i];
            }
            return undefined;
        },
        //聊天
        goToChat:function(item) {
            this.curr_friend = this.getFriend(item.id);
            this.addRecord(item.id);
            this.curr_records = this.curr_friend.records;
            this.gochat();
        },
        //添加聊天信息
        addRecord:function (target,record) {
            let friend = this.getFriend(target)
            if(!friend.records){
                var that = this;
                //获取聊天信息
                $.post(_ctx+"message/list",{
                    send:this.curr_user.id,
                    recive:target
                },function (data) {
                    if(data.status==200){
                        friend.records = data.data;
                    }else {
                        that.alertRemind("获取消息失败");
                    }
                });
            }else{
                record && friend.records.push(record);
            }
            if(friend != this.curr_friend){
                friend.unread_num++;
            }
            this.scroll();
        },
        //展开收拢
        trigger:function(flag) {
            //处理分组展开逻辑
            if(this.groupflag.search(flag)!=-1){
                this.groupflag=this.groupflag.replace(flag,'');
            }else if(this.groupflag.search(flag)==-1){
                this.groupflag += flag;
            }
        },
        //进入聊天页
        gochat:function(){
            this.tab='chat';
            this.anistyle='chattol';
            this.scroll();
        },
        //滚动到低端
        scroll:function () {
            if(this.tab=='chat')
                this.$nextTick(function() {
                    let msg = document.getElementById('gundong');   // 获取对象
                    msg.scrollTop = msg.scrollHeight;               // 滚动高度
                });
        },
        //发消息
        sendMessage:function(){
            let newMsg = {
                send: this.curr_user.id,
                data: {
                    content:this.message,
                    headImg:this.curr_user.headImg
                }
            }
            this.addRecord(this.curr_friend.id,newMsg);
            this.scroll();
            let msg={
                code:"200",
                payLoad:{
                    source:this.curr_user.id,
                    target:this.curr_friend.id,
                    type: "P",
                    data:newMsg.data
                }
            }
            if(socket.readyState == WebSocket.OPEN)
                socket.send(JSON.stringify(msg));
            this.message = '';
        },
        //用户上线
        online:function (data) {
            this.curr_user.history.push(data);
            this.alertRemind(data.username+"上线");
        },
        //用户下线
        offline:function (data) {
            for(let i in this.curr_user.history){
                if(this.curr_user.history[i].id==data.id){
                    this.alertRemind(this.curr_user.history[i].username+"下线");
                    this.curr_user.history.splice(i,1);
                }
            }
        },
        //提示
        alertRemind:function (message) {
            this.remind.message=message;
            this.remind.show=true;
            let that = this;
            setTimeout(function () {
                that.remind.message='';
                that.remind.show=false;
            },1000);
        }
    },
    created:function(){
        var that = this;
        if(!window.WebSocket){
            window.WebSocket = window.MozWebSocket;
        }
        if(window.WebSocket){
            socket = new WebSocket(socket_addr);
            socket.onmessage = function(event){
                let obj=eval('(' + event.data + ')');
                console.log("收到消息：",obj);
                if(obj.code!="200"){
                    that.alertRemind(obj.message);
                    return;
                }
                if (obj.payLoad.type=="P"){
                    that.addRecord(obj.payLoad.source,{
                        send:obj.payLoad.source,
                        data: obj.payLoad.data
                    });
                }else if (obj.payLoad.type=="G") {
                    showGroupChat(obj.payLoad);
                }else if (obj.payLoad.type=="ON"){
                    that.online(obj.payLoad.data)
                } else if (obj.payLoad.type=="OFF"){
                    that.offline(obj.payLoad.data)
                }else if (obj.payLoad.type=="SYS") {
                    // channelId=obj.data.channelId;
                }
            };
            socket.onopen = function(){
                that.alertRemind("打开WebSoket 服务正常，浏览器支持WebSoket!");
            };
            socket.onclose = function(){
                that.alertRemind("你被挤下线了");
            };
        }else{
            that.alertRemind("您的浏览器不支持WebSocket协议！");
        }
    },
    watch:{
        curr_friend: {
            handler(n,o){},
            deep: true
        }
    }
});