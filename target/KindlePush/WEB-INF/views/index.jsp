<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <title>Kindle Ebooks Push</title>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/layui.css">
</head>

<body style="width: 100%;height: 100%;">
<div style="width: 100%;height: 100%;">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
        <legend>Kindle Ebooks Push</legend>
    </fieldset>
    <div class="layui-upload">
        <div class="layui-upload-list">
            <table class="layui-table">
                <thead>
                <tr>
                    <th>书籍文件名</th>
                    <th>大小</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="demoList"></tbody>
            </table>
        </div>
        <button type="button" class="layui-btn layui-btn-normal" id="testList">选择多书籍文件</button>
        <fieldset class="layui-field-title" style="margin-top: 30px;">
            <legend>Kindle 账号信息</legend>
        </fieldset>
        <div class="layui-form-item">
            <label class="layui-form-label">Kindle 邮箱</label>
            <div class="layui-input-inline">
                <input type="text" id="email" name="email" required lay-verify="required" placeholder="请输入邮箱地址" autocomplete="off"
                       class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux">kindle分配给你的收件邮箱：)</div>
        </div>
        <button type="button" class="layui-btn" id="testListAction">开始提交</button>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/layui.js" charset="utf-8"></script>
<script type="text/javascript">
    layui.use('upload', function () {
        var $ = layui.jquery,
            upload = layui.upload;

        //多文件列表示例
        var demoListView = $('#demoList'),
            uploadListIns = upload.render({
                elem: '#testList',
                url: 'http://localhost:8888/KindlePush_war_exploded/push',
                method: 'POST', //可选项。HTTP类型，默认post
                accept: 'file',
                multiple: true,
                auto: false,
                bindAction: '#testListAction',
                before: function (obj) {
                    var data = {};
                    data.email = email.value;
                    this.data = data;
                },
                choose: function (obj) {
                    var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                    //读取本地文件
                    obj.preview(function (index, file, result) {
                        var tr = $(['<tr id="upload-' + index + '">', '<td>' + file.name + '</td>', '<td>' + (file.size / 1014).toFixed(1) + 'kb</td>', '<td>等待上传</td>', '<td>', '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>', '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>', '</td>', '</tr>'].join(''));

                        //单个重传
                        tr.find('.demo-reload').on('click', function () {
                            obj.upload(index, file);
                        });

                        //删除
                        tr.find('.demo-delete').on('click', function () {
                            delete files[index]; //删除对应的文件
                            tr.remove();
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        });

                        demoListView.append(tr);
                    });
                },
                done: function (res, index, upload) {
                    if (res.code == 0) { //上传成功
                        var tr = demoListView.find('tr#upload-' + index),
                            tds = tr.children();
                        tds.eq(2).html('<span style="color: #5FB878;">推送成功</span>');
                        tds.eq(3).html(''); //清空操作
                        return delete this.files[index]; //删除文件队列已经上传成功的文件
                    }
                    this.error(index, upload, res.msg);
                },
                error: function (index, upload, msg) {
                    var tr = demoListView.find('tr#upload-' + index),
                        tds = tr.children();
                    tds.eq(2).html('<span style="color: #FF5722;">推送失败</span>');
                    alert(msg);
                    tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                }
            });
    });
</script>
</body>

</html>