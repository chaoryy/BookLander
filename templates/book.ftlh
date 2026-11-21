<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${(book.name)!"Book"}</title>
</head>
<body>
<div>
    <#if book??>
        <#if book.cover?has_content>
            <div>
                <img src="${book.cover}" alt="${book.name!""}">
            </div>
        </#if>
        <div>
            <h2>${book.name!""}</h2>
            <p>Author: ${book.author!""}</p>
            <p>Description:<br>${book.description!""}</p>
        </div>
    <#else>
        <p>Book not found.</p>
    </#if>
</div>
</body>
</html>