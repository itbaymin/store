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
        friendgroups: _user.groups,
        //群聊列表
        grolists:[
            {
                username: 'XX即时通讯官方群',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)",
                uid:'00002',
                flag:'group'
            }
        ],
        //消息列表
        items: _user.history
    },
    created:function(){
        if(!window.WebSocket){
            window.WebSocket = window.MozWebSocket;
        }
        if(window.WebSocket){
            socket = new WebSocket(socket_addr);
            socket.onmessage = function(event){
                let obj=eval('(' + event.data + ')');
                if (obj.payLoad!=null||obj.payLoad!=undefined){
                    if (obj.payLoad.type==Protocol.PrivateChat){
                        showGreeting(obj);
                    }else if (obj.payLoad.type==Protocol.GroupChat) {
                        showGroupChat(obj);
                    }
                }
                if (obj.type=="ON"){
                    addUser(obj.data)
                } else if (obj.type=="OFF"){
                    deleteUser(obj.data)
                }else if (obj.type=="SYS") {
                    channelId=obj.data.channelId;
                }else if (obj.type==Protocol.GroupApply){
                    showGroupApply(obj.data);
                }
            };
            socket.onopen = function(){
                this.showRemind("打开WebSoket 服务正常，浏览器支持WebSoket!");
            };
            socket.onclose = function(){
                this.showRemind("你被挤下线了");
            };
        }else{
            this.showRemind("您的浏览器不支持WebSocket协议！");
        }
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
        //下面是指向安卓的方法
        //消息列表朋友列表(朋友&群聊)条目被点击跳转到聊天窗口--传入后由java解析
        goToChat:function(item) {
            this.gochat(JSON.stringify(item));
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
        },
        sendMsg:function(){
            this.items.push({
                username: '年华',
                content: this.message,
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)",
                uid:'00003',
                time:'晚上20:30',
                mesnum:0
            });
            this.$nextTick(function() {
                let msg = document.getElementById('gundong') // 获取对象
                msg.scrollTop = msg.scrollHeight // 滚动高度
            });
        },
        showRemind:function (message) {
            this.remind.message=message;
            this.remind.show=true;
        },
        hideRemind:function () {
            this.remind.message='';
            this.remind.show=false;
        }
    }
});