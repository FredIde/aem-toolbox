<%@ include file="/libs/foundation/global.jsp" %>
<jsp:useBean id="ui" class="nl.tricode.aem.tools.components.ui.AemToolsUiComponent">
    <jsp:setProperty name="ui" property="request" value="${slingRequest}"/>
    <jsp:setProperty name="ui" property="response" value="${slingResponse}"/>
</jsp:useBean>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Tricode AEM toolbox</title>
    <cq:includeClientLib categories="cq.gui.common.admin.navigationpanel"/>
    <cq:includeClientLib categories="aemtools.ui"/>
</head>

<body>
    <header class="top">
        <div class="logo"><a href="/"><i class="icon-marketingcloud medium"></i></a></div>
        <nav class="crumbs">
            <a href="/">Adobe Marketing Cloud</a>
            <a href="/">Experience Manager</a>
            <a href="/etc/aemtools.html/">Tricode AEM toolbox</a>
        </nav>
    </header>

    <div class="page" role="main">
        <div id="rail" class="rail left" role="complementary">
            <div class="wrap">
                <div class="rail-view group active" data-view="navigation" role="tabpanel" aria-hidden="false" aria-expanded="true">
                    <nav class="feature">
                        <c:forEach items="${ui.plugins}" var="plugin">
                            <span><a href="/etc/aemtools.html/${plugin.class.simpleName}">${plugin.name}</a></span>
                        </c:forEach>
                    </nav>
                </div>
            </div>
        </div>

        <div id="content" class="content foundation-content">
            <div class="foundation-content-current">
                <c:if test="${ui.showDefaultContent}">
                    <c:set var="showDefaultContent" value="${true}"/>
                    <nav data-init="toolbar" class="toolbar mode-default">
                        <div class="center">
                            <h1>Instance overview</h1>
                        </div>
                    </nav>

                    <div class="content-container toollist">
                        <div class="at-content">
                            <div class="at-stat">Aem version: ${ui.aemVersion}</div>
                            <div class="at-stat">AEM Tools version: ${ui.aemToolsVersion}</div>
                            <div class="at-stat">
                                Sling runmodes:<br/>
                                <ul>
                                    <c:forEach items="${ui.runModes}" var="rm">
                                        <li>${rm}</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not showDefaultContent}">
                    <sling:include path="/apps/${ui.componentPathToInclude}" resourceType="${ui.componentPathToInclude}" />
                </c:if>
            </div>
        </div>
    </div>
</body>

</html>