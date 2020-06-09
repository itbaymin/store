var socket;
new Vue({
    el: '#app',
    data: {
        remind:{show:false,message:''},
        remind1:[],
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
        searchFlag:'',          //是否处于搜索状态的标记
        searchInput:'',         //搜索输入的内容
        searchedUsers:[],       //扩展空间的搜索结果
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
        focusSearch:function () {
            this.searchFlag = 'focus';
            this.$nextTick(function() {
                this.$refs.search.focus();
            })
        },
        cancelSearch:function () {
            this.searchFlag = '';
            this.searchInput="";
        },
        clearSearch:function () {
            this.searchInput="";
        },
        //搜索
        doSearch:function () {
            let that = this;
            if(that.tab=='tab1'){          //搜索聊天

            }else if(that.tab=='tab2'){   //搜索朋友

            }else {                        //搜索在线用户
                $.post(_ctx+"searchOnlineUser",{keyWord:that.searchInput},function (data) {
                    if(data.status==200){
                      that.searchedUsers = data.data.filter(item => item.id!=that.curr_user.id);
                    }else {
                      that.searchedUsers = [];
                      that.alertRemind(data.message);
                    }
                });
            }
        },
        //添加朋友
        applyFriend:function (item) {
            let msg={
                code:"200",
                payLoad:{
                    source:this.curr_user.id,
                    target:item.id,
                    type: "FAPPLY",
                    data:{username:this.curr_user.username}
                }
            };
            if(socket.readyState == WebSocket.OPEN)
                socket.send(JSON.stringify(msg));
        },
        addFriend:function (user) {
            this.curr_user.history.push(user);
            friends.push(user);
            console.log(this.curr_user.groups);
            if(this.curr_user.groups)
                for(let i in this.curr_user.groups){
                    if(this.curr_user.groups[i].name=='默认') {
                        this.curr_user.groups[i].friends.push(user);
                    }
                }
            else
                this.curr_user.groups = [{
                    name:'默认',
                    flag:1,
                    friends:[user]
                }];
            console.log(this.curr_user.groups);
        },
        //tab跳转
        goToTab: function(tab) {
            if(this.tab > tab)
                this.anistyle='rtol';
            else if(this.tab < tab)
                this.anistyle='ltor';
            else
                this.anistyle='';
            this.cancelSearch();
            this.tab=tab;
            document.documentElement.scrollTop=0;
            document.body.scrollTop=0;
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
                for (let i in this.curr_user.history){
                    if(this.curr_user.history[i].id==target)
                        this.curr_user.history[i].unReadNum++;
                }
            }

            console.log(this.curr_user.history);
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
            };
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
            let friend = this.getFriend(data.id);
            if(friend) {
                this.curr_user.history.push(friend);
                this.alertRemind(data.username + "上线");
            }
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
        //同意好友请求
        agreeFApply:function (data) {
            let that = this;
            $.post(_ctx+"agreeeFriend",data,function (data) {
                if(data.status==200){
                    that.alertRemind("添加成功");
                }else {
                    that.alertRemind("获取消息失败");
                }
            })
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
        },
        addRemind1:function (payload) {
            this.remind1.push({
                message: payload.data.username,
                apply: payload.source,
                agree: payload.target
            });
        },
        cancelRemind1:function (index) {
            this.remind1.splice(index,1);
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
                }else if (obj.payLoad.type=="FAPPLY") {     //好友申请
                    //弹出是否同意
                    that.addRemind1(obj.payLoad);
                }else if (obj.payLoad.type=="FAGREE") {     //添加好友
                    that.addFriend(obj.payLoad.data);
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
        },
        curr_user: {
            handler(n,o){},
            deep: true
        }
    }
});