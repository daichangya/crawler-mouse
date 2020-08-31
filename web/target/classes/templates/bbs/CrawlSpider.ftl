<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>获取论坛数据</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <style>
        html, body {
            height: 100%;
            width: 100%
        }

        .par {
            height: 100%;
            width: 100%
        }

        .tmp1 {
            height: 10%;
            width: 100%
        }

        .container {
            height: 80%;
            width: 80%
        }

        .form1 {
            height: 100%;
            width: 100%
        }
    </style>
</head>
<body>
<div class="container" style="height: 80%">
    <form class="form1" method="post">
        <div class="form-group">
            <label for="exampleFormControlInput1">
                <#if message_ids??>
                    <span style="color: red">${message_ids}</span>
                <#else>
                     请输入认证码
                </#if>
            </label>
            <input type="text" name="ids" class="form-control" id="exampleFormControlInput1">
        </div>
        <div class="form-group">
            <label for="exampleFormControlInput1">
                请输入email
            </label>
            <input type="text" name="email" class="form-control" id="exampleFormControlInput1">
        </div>
	<#--<div class="form-group">-->
	<#--<label for="exampleFormControlSelect1">请选择条数，建议选择30条</label>-->
	<#--<select name="select1" class="form-control" id="exampleFormControlSelect1">-->
	<#--<option name="15">15条</option>-->
	<#--<option name="30" selected="selected">30条</option>-->
	<#--<option name="45">45条</option>-->
	<#--<option name="all">全部</option>-->
	<#--</select>-->
	<#--</div>-->
        <div class="form-group" style="height: 50%">
            <label for="exampleFormControlTextarea1">
                <#if message_urls??>
                    <span style="color: red">${message_urls}</span>
                <#else>
                     请输入URL地址，换行符分割
                </#if>
            </label>
            <textarea style="height: 100%" name="urls" class="form-control" id="exampleFormControlTextarea1"
                      rows="3"></textarea>
        </div>
        <div class="form-group" style="height: 2%"></div>
        <div class="form-group" style="height: 10%">
            <input id="button1" style="height: 70%; width: 10%" type="submit" value="提交">
        </div>
    </form>
</div>
</div>
</body>
</html>
