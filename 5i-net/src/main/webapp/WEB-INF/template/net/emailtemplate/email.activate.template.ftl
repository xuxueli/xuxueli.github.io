<#include "/net/common/common.host.ftl" >
<#import "/net/common/common.macro.ftl" as netCommon>
<#assign toUrl="${host_url}/safe/emailActivate.html?email=${userMain.email}&sendCode=${sendCode}">
<html><head></head><body><p><strong>亲爱的《我爱》用户:</strong></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;您正在操作您的《我爱》邮箱账号：<font color='red'>${userMain.email?if_exists}</font></p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;操作类型[<font color='red'>账号激活</font>]：<a href='${toUrl}' target="_blank" >确定</a>(请在72小时内完成)
<p>&nbsp;&nbsp;&nbsp;&nbsp;如果打不开激活页面,您可以复制下面的链接到浏览器地址栏中进行激活操作：
<p>&nbsp;&nbsp;&nbsp;&nbsp;[${toUrl}]
<p>&nbsp;&nbsp;&nbsp;&nbsp;本邮件由系统自动发出，请勿回复。
<p>&nbsp;&nbsp;&nbsp;&nbsp;感谢您使用《我爱》提供服务，有任何问题可联系管理员！
<p align='right'>《我爱》</p></body></html>
