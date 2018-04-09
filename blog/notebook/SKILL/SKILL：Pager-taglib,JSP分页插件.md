
### 简介
Pager-taglib 2.0 是一套jsp分页标签库，可以灵活地实现多种不同风格的分页导航页面，也可以自定义风格样式。并且它可以很好的与服务器分页逻辑分离，它既可以对后台传入的集合进行分页，也可以从数据库中取出要显示那一页的数据。

### 标签介绍
- 1、<pg:pager>：这个标签用来设置分页的总体参数，一切分页标签都在其内工作。
    - url:分页的链接根地址，pager标签会在这个链接的基础上附加分页参数
    - items:总记录数，pager标签正是根据这个值来计算分页参数的
    - maxPageItems:每页显示的行数，默认为10
    - maxIndexPages:在循环输出页码的时候，最大输出多少个页码，默认是10
    - isOffset：与pg:item配套使用 
    - export：这个属性比较重要，文档也对此作好相对长篇幅的说明。这个属性是让标签给你暴露什么变量，当然这些变量是有选择的，如在Pager标签里，可以暴露出来的变量有pageOffset及pageNumber，即页码偏移量及页码。通过这两个变量名，可以在Jsp或Java里面从Request里获得。Export属性接受的值还有表达式，如currentPage=pageNumber表示，把pageNumber的值暴露出来，并赋给一个叫CurrentPage的变量，这个变量将被保存到Request中，在Jsp或Java中可以得到。
    
- 2.pg:param：用来设置将要加入到URL的参数。使用Name属性指定即可，用于参数传递。

- 3.pg:index：这个标签说明分页条显示的内容，在这里你可以设置各种风格的分页显示方式。

- 4.pg:first：第一页标签
    - pageUrl - 分页链接URL地址
    - pageNumber - 页码
    - firstItem - 首页第一行的索引值
    - lastItem - 首页最后一行的索引值
    
- 5.pg:pre：上一页标签
    - pageUrl - 分页链接URL地址
    - pageNumber - 页码
    - firstItem - 前页第一行的索引值
    - lastItem - 前页最后一行的索引值

- 6.pg:pages：这个标签用来循环输出页码信息
    - pageUrl - 分页链接URL地址
    - pageNumber - 页码
    - firstItem
    - pageNumber这个页码指定的那一页的第一行的索引值
    - lastItem
    - pageNumber这个页码指定的那一页的最后一行的索引值

- 7.pg:next：下一页标签
    - pageUrl - 分页链接URL地址
    - pageNumber - 页码
    - firstItem - 下页第一行的索引值
    - lastItem - 下页最后一行的索引值

- 8.pg:last：最后一页标签
    - pageUrl - 分页链接URL地址
    - pageNumber - 页码
    - firstItem - 尾页第一行的索引值
    - lastItem - 尾页最后一行的索引值
    

### 使用步骤
官网：http://jsptags.com/tags/navigation/pager/  （官网貌似挂了，用maven吧）
- 1、maven依赖
```
<dependency>
    <groupId>jsptags</groupId>
    <artifactId>pager-taglib</artifactId>
    <version>2.0</version>
</dependency>
```

- 2、引入标签库

可以结合C标签一起使用
```
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
```

- 3.1、从后台传入List结果集，在页面上通过<pg:item>对List进行自动分页
```
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>

<pg:pager url="${pageContext.request.contextPath}/sysParamAction.do" index="center"
    maxPageItems="10" maxIndexPages="10" isOffset="<%=false%>"
    export="pageOffset,currentPageNumber=pageNumber" scope="request">
    <pg:param name="m" value="findSysparams" />

    <table>
        <tr>
            <td colspan="2">XX列表</td>
        </tr>
        <tr>
            <td>XX</td>
            <td>XX</td>
        </tr>
        <c:forEach items="${list }" var="list" varStatus="listStatus">
            <pg:item>
                <c:if test="${1 == listStatus.count % 2 }">
                    <tr class="tab_list_tr2" 
                        onmouseover= this.className = 'tab_list_tr_hover';;
                        onmouseOut= this.className = 'tab_list_tr2';;>
                </c:if>
                <c:if test="${0 == listStatus.count % 2 }">
                    <tr class="tab_list_tr"
                        onmouseover= this.className = 'tab_list_tr_hover';;
                        onmouseOut= this.className = 'tab_list_tr';;>
                </c:if>
                    <td>${list.xx }</td>
                    <td>${list.xx }</td>
                </tr>
            </pg:item>
        </c:forEach>
    </table>

    <pg:index>
        <pg:first>
            <a href="<%=pageUrl%>"><img src="${ctx}/images/grid/firstPage.gif" border="0"></a>
        </pg:first>
        <pg:prev>
            <a href="<%=pageUrl%>"><img src="${ctx}/images/grid/prevPage.gif" border="0"></a>
        </pg:prev>
        <pg:pages>
            <c:choose>
                <c:when test="${pageNumber eq currentPageNumber}">
                    <font color="red">[<%=pageNumber%>]</font>
                </c:when>
                <c:otherwise>
                    <a href="<%=pageUrl%>"><%=pageNumber%></a>
                </c:otherwise>
            </c:choose>
        </pg:pages>
        <pg:next>
            <a href="<%=pageUrl%>"><img src="${ctx}/images/grid/nextPage.gif" border="0"></a>
        </pg:next>
        <pg:last>
            <a href="<%=pageUrl%>"><img src="${ctx}/images/grid/lastPage.gif" border="0"></a>
        </pg:last>
        一共${fn:length(list) }条记录
    </pg:index>
</pg:pager>
```


- 3.2、通过把 pageSize,pageNo两参数传给后台进行数据库分页

文件：pager.jsp
```
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager" %> 
<%@ taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
 <pg:pager url="${parm.url }" items="${total}"  maxPageItems="${pagesize }" maxIndexPages="15" export="currentPageNumber=pageNumber">    
      <tr>
        <td width="300px" align="left"><span class="STYLE22">&nbsp;&nbsp;&nbsp;&nbsp;
	        共有<strong> ${total}</strong> 条记录，
	        当前第<strong> ${currentPageNumber}</strong> 页，
	        <pg:last>
	        共 <strong>${pageNumber}</strong> 页
	        </pg:last>
	        </span></td>
        <td width="500px" align="right">
	        <table width="100%" border="0" cellpadding="0" cellspacing="0">
				
				<pg:first>
					<a id="firstPage"  href="${pageUrl }"><img src="images/main_54.gif" width="40" height="15" /></a>
				</pg:first>
				
				<pg:prev>	
					<a href="${pageUrl }"><img src="images/main_56.gif" width="45" height="15" /></a>
				</pg:prev>
				
				<pg:pages>
					<c:choose>
						<c:when test="${currentPageNumber eq pageNumber}">
							<font color="red" >${pageNumber }</font>
						</c:when>
						<c:otherwise>
							<a href="${pageUrl }">${pageNumber }</a>
						</c:otherwise>
					</c:choose>
				</pg:pages>
				
				<pg:next>
					<a href="${pageUrl }"><img src="images/main_58.gif" width="45" height="15" /></a>
				</pg:next>
				
				<pg:last>
					<a href="${pageUrl }"><img src="images/main_60.gif" width="40" height="15" /></a>
				</pg:last>
				
				<select name="pagesize" onchange="selectPagesize(this)">
					<c:forEach begin="5" end="50" step="5" var="i">
						<option value="${i }" <c:if test="${i eq pagesize }">selected</c:if>>${i}</option>
					</c:forEach>
				</select>
				
			</table>
        </td>
      </tr>
     </pg:pager>
```

页面嵌入分页模块:
```
<jsp:include page="pager.jsp">
    <jsp:param name="url" value="StaffInformationQueryServlet"></jsp:param>
</jsp:include>
```

后端代码：

```
// offset/pagesize
int offset = Integer.parseInt(req.getParameter("pager.offset"));
int pagesize = req.getSession().setAttribute("pagesize",req.getParameter("pagesize"));

// 响应数据
List<Data> result = ...; // 返回的数据
int total = ... ; // 总记录数
int currentPage = ...; // 当前页数
int maxPage = ...; // 最大页
```






