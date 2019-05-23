<html>
    <head>
        <title>freemarker模板测试</title>
    </head>
    <body>
        freemarker的模板   ${name}

        <hr/>
        年龄：${age}
        <#if age < 18>
            未成年
            <#elseif (age >= 18 && age < 40)>
            成年
            <#elseif (age >= 40 && age < 60)>
            中年
            <#else>
            老年
        </#if>
        <hr/>

        <#list books as book>
            ${book}
        </#list>

        <hr/>
        ${now?date}
        ${now?time}
        ${now?datetime}
        ${now?string('yyyy年MM月dd日 HH时mm分ss秒SSS毫秒')}

        <hr/>
        ${money?string('￥#,###.##')}
    </body>
</html>