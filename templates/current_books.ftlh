<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${(employee.name)!"Current Books"}</title>
</head>
<body>
    <h1>${(employee.name)!""} ${(employee.lastName)!""} current books</h1>
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
</body>
</html>