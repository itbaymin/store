var socket;
new Vue({
    el: '#app',
    data: {
        remind:{
            show:false,
            message:''
        },
        message: '',
        groupflag:'0',
        anistyle:'',            //切换动画样式类
        active:'message',       //当前页面类型
        sub_active:'friend',    //当前选项卡
        chat_type:'',           //聊天类型
        chat_id: 0,             //聊天对象id
        curr_friend:{},
        curr_records:[],
        curr_user:_user,
        TopImg:[
            {
                src:"background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)",
                link:"http://www.fanze.online",
            },
            {
                src:"border-radius: 5%;background-image:url(https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555324144&di=430a23b81be40e36546a7a13f62d87a2&imgtype=jpg&er=1&src=http%3A%2F%2Fuploads.5068.com%2Fallimg%2F1802%2F78-1P209141R8.jpg)",
                link:"http://fanze.online",
            }
        ],
        //群聊列表
        grolists:[
            {
                headImg:"background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)",
                uid:'00002'
            }
        ]
    },
    methods: {
        trygroupflag:function(groupflag){
            if(this.groupflag.search(groupflag)!=-1)
                return true;
            else
                return false;
        },
        //tabbar消息列表被点击
        msgbtnde: function() {
            if(!(this.active=='message'))
                this.anistyle='rrtol';
            this.active='message';
            document.documentElement.scrollTop=0;
            document.body.scrollTop=0;
        },
        //tabbar我的朋友被点击
        myfribtnde: function() {
            //判断切换前页
            if(!(this.active=='friend'))
                this.anistyle='ltor';
            this.active='friend';
            document.documentElement.scrollTop=0;
            document.body.scrollTop=0;
        },
        //我的朋友页中的朋友列表被点击
        myfriFri:function() {
            this.sub_active = 'friend';
        },
        //我的朋友页中的群聊列表被点击
        myfriGro:function() {
            this.sub_active = 'group';
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
                })
            }else{
                record && friend.records.push(record);
            }
            this.scroll();
        },
        trigger:function(flag) {
            //处理分组展开逻辑
            if(this.groupflag.search(flag)!=-1){
                this.groupflag=this.groupflag.replace(flag,'');
            }else if(this.groupflag.search(flag)==-1){
                this.groupflag += flag;
            }
        },
        //搜索框被点击
        goToSearch:function(searchfalg) {
            //android.gosearch(searchfalg);
        },
        //我的朋友页顶部图片被点击
        topImgClicked:function(link) {
            //android.gosearch(link);
        },
        gochat:function(){
            this.active='chat';
            this.anistyle='chattol';
            this.scroll();
        },
        scroll:function () {
            if(this.active=='chat')
                this.$nextTick(function() {
                    let msg = document.getElementById('gundong') // 获取对象
                    msg.scrollTop = msg.scrollHeight // 滚动高度
                });
        },
        sendMsg:function(){
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
            console.log(data)
            this.curr_user.history.push(data);
            this.alertRemind(data.username+"上线");
        },
        offline:function (data) {
            for(let i in this.curr_user.history){
                if(this.curr_user.history[i].id==data.id){
                    this.alertRemind(this.curr_user.history[i].username+"下线");
                    this.curr_user.history.splice(i,1);
                }
            }
        },
        //用户下线
        showRemind:function (message) {
            this.remind.message=message;
            this.remind.show=true;
        },
        alertRemind:function (message) {
            this.showRemind(message);
            setTimeout(this.hideRemind,1000);
        },
        hideRemind:function () {
            this.remind.message='';
            this.remind.show=false;
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
                that.showRemind("打开WebSoket 服务正常，浏览器支持WebSoket!");
            };
            socket.onclose = function(){
                that.showRemind("你被挤下线了");
            };
        }else{
            that.showRemind("您的浏览器不支持WebSocket协议！");
        }
    },
    watch:{
        curr_friend: {
            handler(n,o){
                console.log(n,o);
            },
            deep: true
        }
    }
});