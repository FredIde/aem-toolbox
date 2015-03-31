<%@ include file="/libs/foundation/global.jsp" %>
<jsp:useBean id="psize" class="nl.tricode.aem.tools.plugins.packagesize.PackageSizeComponent">
    <jsp:setProperty name="psize" property="request" value="${slingRequest}"/>
</jsp:useBean>
<nav data-init="toolbar" class="toolbar mode-default">
    <div class="center">
        <h1>Package size estimation</h1>
    </div>
</nav>

<div class="foundation-collection-container content-container">
    <div class="foundation-collection foundation-layout-list list">
        <div class="grid-0">
            <c:if test="${not psize.showResult}">
                <header class="card-page selectable">
                    <div class="label">
                        <div data-sort-selector=".label h4" data-title="Package" class="main">Packages that have not been built or have a modified filter:</div>
                    </div>
                </header>
                <form method="get">
                    <c:forEach items="${psize.candidatePackages}" var="package">
                        <div class="pkgsize-listitem">
                            <input type="checkbox" class="pkgsize-checkbox" name="packages" value="${package.uuid}" id="${package.uuid}"/> <label for="${package.uuid}">${package.name} <span class="pkgsize-meta">Group: ${package.group}, version: ${package.version}</span></label> <br/>
                        </div>
                    </c:forEach>
                    <button class="pkgsize-submit" data-name="submit" type="submit">Calculate</button>
                </form>
            </c:if>
            <c:if test="${psize.showResult}">
                <header class="card-page selectable">
                    <div class="label">
                        <div data-sort-selector=".label h4" data-title="Package" class="main">Package size estimation result</div>
                    </div>
                </header>
                <c:forEach items="${psize.selectedPackageInfo}" var="package">
                    <div class="pkgsize-listitem">
                        Package: ${package.name} <span class="pkgsize-meta">Group: ${package.group}, version: ${package.version}</span> - Estimated size: <c:if test="${package.size eq 0}">&lt; 1</c:if><c:if test="${package.size ge 1}">${package.size}</c:if> MB<br/>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>