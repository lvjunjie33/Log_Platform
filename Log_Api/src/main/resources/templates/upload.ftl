<html>
<head>
    <meta charset="utf-8">
    <title>HTML5大文件分片上传示例</title>
    <script src="http://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="../static/js/md5.js"></script>
    <script type="text/javascript">
        var i = -1;
        var shardSize = 1 * 1024 * 1024;    //以1MB为一个分片
        var succeed = 0;
        var dataBegin;  //开始时间
        var dataEnd;    //结束时间
        var action = false;
        var page = {
            init: function () {
                $("#upload").click(function () {
                    dataBegin = new Date();
                    var file = $("#file")[0].files[0];  //文件对象
                    isUpload(file);
                });
            }
        };
        $(function () {
            page.init();
        });
        function isUpload (file) {
            //构造一个表单，FormData是HTML5新增的
            var form = new FormData();
            var r = new FileReader();
            r.readAsBinaryString(file);
            $(r).load(function(e){
            
                var blob = e.target.result;
                var md5 = hex_md5(blob);
                form.append("md5", md5);
                //Ajax提交
                $.ajax({
                    url: "isUpload",
                    type: "POST",
                    data: form,
                    async: true,        //异步
                    processData: false,  //很重要，告诉jquery不要对form进行处理
                    contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                    success: function(data){
                        var uuid = data.fileId;
                        if (data.flag == "0") {
                            //没有上传过文件
                            realUpload(file,uuid,md5,data.date);
                        } else if(data.flag == "1") {
                            //已经上传部分
                            realUpload(file,uuid,md5,data.date);
                        } 
                        //else if(data.flag == "2") {
                            //文件已经上传过
                          //  alert("文件已经上传过,秒传了！！");
                        //}
                    },error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert("服务器出错!");
                    }
                })
            })
        };
        
        function realUpload(file, uuid, md5, date) {
            name = file.name;
            size = file.size;
            var shardCount = Math.ceil(size / shardSize);  //总片数
            if (i > shardCount) {
                return;
            } else {
                if (!action) {
                    i += 1;  //只有在检测分片时,i才去加1; 上传文件时无需加1
                }
            }
            //计算每一片的起始与结束位置
            var start = i * shardSize;
            var end = Math.min(size, start + shardSize);
            //构造一个表单，FormData是HTML5新增的
            var form = new FormData();
            if (!action) {
                form.append("action", "check");  //检测分片是否上传
                $("#param").append("<br/>" + "action==check " + "&nbsp;&nbsp;");
            } else {
                form.append("action", "upload");  //直接上传分片
                form.append("data", file.slice(start, end));  //slice方法用于切出文件的一部分
                $("#param").append("<br/>" + "action==upload ");
            }
            form.append("uuid", uuid);
            form.append("md5", md5);
            form.append("date", date);
            form.append("name", name);
            form.append("size", size);
            form.append("total", shardCount);  //总片数
            form.append("index", i+1);        //当前是第几片

            var index = i+1;
            $("#param").append("index==" + index);
            //按大小切割文件段　　
            var data = file.slice(start, end);
            var r = new FileReader();
            r.readAsBinaryString(data);
            $(r).load(function (e) {
                var bolb = e.target.result;
                var partMd5 = hex_md5(bolb);
                form.append("partMd5", partMd5);
                //Ajax提交
                $.ajax({
                    url: "upload",
                    type: "POST",
                    data: form,
                    async: true,        //异步
                    processData: false,  //很重要，告诉jquery不要对form进行处理
                    contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                    success: function (data) {
                        var fileuuid = data.fileId;
                        var flag = data.flag;
                        if (flag != "2") {
                            //服务器返回该分片是否上传过
                            if (flag == "0") {
                                //未上传,继续上传
                                action = true;
                            } else if (flag == "1") {
                                //已上传
                                action = false;
                                ++succeed;
                                $("#output").text(succeed + " / " + shardCount);
                            }
                            realUpload(file, uuid, md5, date);
                        } else {
                            ++succeed;
                            $("#output").text(succeed + " / " + shardCount);
                            //服务器返回分片是否上传成功
                            if (succeed  == shardCount) {
                                dataEnd = new Date();
                                $("#uuid").append(fileuuid);
                                $("#useTime").append((dataEnd.getTime() - dataBegin.getTime())/1000);
                                $("#useTime").append("s")
                                $("#param").append("<br/>" + "上传成功！");
                            }
                        }
                    }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert("服务器出错!");
                    }
                });
            })
        }
    </script>
</head>

<body>

<input type="file" id="file" />
<button id="upload">上传</button>
<br/><br/>
<span style="font-size:16px">上传进度：</span><span id="output" style="font-size:16px"></span>
<span id="useTime" style="font-size:16px;margin-left:20px;">上传时间：</span>
<span id="uuid" style="font-size:16px;margin-left:20px;">文件ID：</span>
<br/><br/>
<span id="param" style="font-size:16px">上传过程：</span>

</body>
</html>