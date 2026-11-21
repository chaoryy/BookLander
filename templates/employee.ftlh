<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${(employee.name)!"Employee"} ${(employee.lastName)!""}</title>
</head>
<body>
    <#if employee??>
        <div>
            <h2>${(employee.name)!""} ${(employee.lastName)!""}</h2>
            <p>ID: ${(employee.id)!"ID not found"}</p>
            <p>Role: ${(employee.role.roleName)!"Not specified"}</p>
            <a href="/employee/prev">Previous books</a>
            <a href="/employee/current">Current books</a>
        </div>
    <#else>
        <p>Employee not found.</p>
    </#if>
</body>
</html>