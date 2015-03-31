<%@ include file="/libs/foundation/global.jsp" %>
<jsp:useBean id="dm" class="nl.tricode.aem.tools.plugins.datamigration.DataMigrationComponent">
    <jsp:setProperty name="dm" property="request" value="${slingRequest}"/>
</jsp:useBean>
<nav data-init="toolbar" class="toolbar mode-default">
    <div class="center">
        <h1>Data migration / patch manager</h1>
    </div>
</nav>

<div class="foundation-collection-container content-container">
    <div class="foundation-collection foundation-layout-list list">
        <div class="grid-0">
            <c:if test="${not dm.showResult}">
                <header class="card-page selectable">
                    <div class="label">
                        <div data-sort-selector=".label h4" data-title="Package" class="main">Select one or more patches</div>
                    </div>
                </header>
                <form method="get">
                    <c:forEach items="${dm.patches}" var="patch">
                        <div class="pkgsize-listitem">
                            <input type="checkbox" class="pkgsize-checkbox" name="patches" value="${patch.class.simpleName}" id="${patch.class.simpleName}"/> <label for="${patch.class.simpleName}">${patch.name}</label> <br/>
                        </div>
                    </c:forEach>
                    <button class="pkgsize-submit" data-name="submit" type="submit" name="action" value="dryrun">Dry run</button>
                    <button class="pkgsize-submit" data-name="submit" type="submit" name="action" value="execute">Execute</button>
                </form>
            </c:if>
            <c:if test="${dm.showResult}">
                <header class="card-page selectable">
                    <div class="label">
                        <div data-sort-selector=".label h4" data-title="Package" class="main">
                            <c:if test="${dm.action eq 'dryrun'}">
                                Dry run result:<br/>
                            </c:if>
                            <c:if test="${dm.action eq 'execute'}">
                                Patch result:
                            </c:if>
                        </div>
                    </div>
                </header>
                <c:forEach items="${dm.patchResults}" var="patch">
                    <div class="dmg-patchresult">
                        <h2 class="dmg-header">${patch.key}</h2>
                        <c:if test="${patch.value.error}">
                            <span class="dmg-error">Patch failed with exception ${patch.value.error.message}</span>
                        </c:if>
                        <c:if test="${not patch.value.error}">
                                <div class="dmg-summary">Migrated ${fn:length(patch.value.migratedPaths)} nodes / skipped ${fn:length(patch.value.skippedPaths)} nodes</div>
                                <c:if test="${dm.action eq 'dryrun'}">
                                    <span>Following nodes would be migrated:</span>
                                    <div class="dmg-paths">
                                        <c:forEach items="${patch.value.migratedPaths}" var="path">
                                            ${path}<br/>
                                        </c:forEach>
                                    </div>
                                    <span>Following nodes would be skipped because they are up-to-date:</span>
                                    <div class="dmg-paths">
                                        <c:forEach items="${patch.value.skippedPaths}" var="path">
                                            ${path}<br/>
                                        </c:forEach>
                                    </div>
                                </c:if>
                        </c:if>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>
</div>