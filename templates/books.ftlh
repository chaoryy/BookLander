<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Books</title>
</head>
<body>
<div>
    <h1>BOOKS</h1>
    <#if books?has_content>
        <#list books as book>
        <a href="/book?bookId=${book.id}">
            <div>
                <div class="book-counter">${book?counter}</div>
                <div class="book-cover">
                    <#if book.cover?has_content>
                        <img src="${book.cover}" alt="${book.name!""}">
                    </#if>
                </div>
                <div>
                    <h3>${book.name!""}</h3>
                    <p>Author: ${book.author!""}</p>
                </div>
                <div>
                    <p>${(book.status.status)!""}</p>
                </div>
            </div>
            </a>
        </#list>
    <#else>
        <p>There are no books :(((</p>
    </#if>
</div>
</body>
</html>