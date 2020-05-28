new Vue({
    el: '#app',
    data: {
        message: '',
        groupflag:'0',
        anistyle:'',//切换动画样式类
        active:'chat',
        sub_active:'friend',
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
        friendgroups:[
            {
                name: '分组1',
                flag: '1',
                friends:[{
                    username: '开发者',
                    imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)",
                    uid:'00001'
                },{
                    username: '张',
                    imgsrc:"background-image:url(http://img2.imgtn.bdimg.com/it/u=2060761043,284284863&fm=26&gp=0.jpg)",
                    uid:'00001'
                }]
            },
            {
                name: '分组2',
                flag: '2',
                friends:[{
                    username: '张',
                    imgsrc:"background-image:url(http://img2.imgtn.bdimg.com/it/u=2060761043,284284863&fm=26&gp=0.jpg)",
                    uid:'00001'
                }]
            }],
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
        items:[
            {
                username: '开发者',
                content:'晚上哪里吃?',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180213/20180213062640_77463.jpg)",
                uid:'00001',
                time:'晚上20:07',
                mesnum:10
            },
            {
                username: '年华',
                content:'移动端怎么样了?',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)",
                uid:'00003',
                time:'晚上20:30',
                mesnum:0
            },
            {
                username: '年华',
                content:'移动端怎么样了?',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)",
                uid:'00003',
                time:'晚上20:30',
                mesnum:0
            },
            {
                username: '年华',
                content:'移动端怎么样了?',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)",
                uid:'00003',
                time:'晚上20:30',
                mesnum:0
            },
            {
                username: '年华',
                content:'移动端怎么样了?',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)",
                uid:'00003',
                time:'晚上20:30',
                mesnum:0
            },
            {
                username: '年华',
                content:'移动端怎么样了?',
                imgsrc:"background-image:url(http://img.52z.com/upload/news/image/20180419/20180419051254_75804.jpg)",
                uid:'00003',
                time:'晚上20:30',
                mesnum:0
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
        }
    }
})