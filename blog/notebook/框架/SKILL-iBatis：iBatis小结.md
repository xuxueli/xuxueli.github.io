##### iBATIS动态标签
```
# 1、dynamic
<dynamic prepend="where"> 
    <isNotEmpty prepend="and" property="name"> 
        name like '%$name$%' 
    </isNotEmpty> 
</dynamic>
...
<dynamic prepend=""> 
    <isNotNull property="_start"> 
        <isNotNull property="_size"> 
            limit #_start#, #_size# 
        </isNotNull> 
    </isNotNull> 
</dynamic>

# 2、iterate
<dynamic prepend="WHERE  ">  
    <iterate property="keywords"  open="(" close=")" conjunction="OR">   
        product=#keywords[]#  
    </iterate>  
</dynamic>  

# 3、isEmpty、isNotEmpty、isNull、isNotNull、isPropertyAvailable、isNotPropertyAvailable（Bean的属性）、isParameterPresent、isNotParameterPresent（对象）
<isNotEmpty prepend="and" property="_img_size_ge"> 
    <![CDATA[   img_size >= #_img_size_ge#  ]]> 
</isNotEmpty>

# 4、isEqual、isGreaterThan、isGreaterEqual、isLessThan、isLessEqual
<isEqual prepend="and" property="_exeable" compareValue="N"> 
    <![CDATA[   (t.finished='11' or t.failure=3) ]]> 
</isEqual>
```
