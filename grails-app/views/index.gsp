<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome to Grails</title>
</head>
<body>
<content tag="nav">
    <li class="nav-item dropdown mr-4">
        <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
           aria-expanded="false">Application Status <span class="caret"></span></a>
        <div class="dropdown-menu">
            <a class="dropdown-item" href="#">Environment: ${grails.util.Environment.current.name}</a>
            <a class="dropdown-item" href="#">App profile: ${grailsApplication.config.grails?.profile}</a>
            <a class="dropdown-item" href="#">App version: <g:meta name="info.app.version"/></a>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item" href="#">Grails version: <g:meta name="info.app.grailsVersion"/></a>
            <a class="dropdown-item" href="#">Groovy version: ${GroovySystem.getVersion()}</a>
            <a class="dropdown-item" href="#">JVM version: ${System.getProperty('java.version')}</a>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item" href="#">Reloading active: ${grails.util.Environment.reloadingAgentEnabled}</a>
        </div>
    </li>
    <li class="nav-item dropdown mr-4">
        <a href="#" class="nav-link  dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
           aria-expanded="false">Artefacts <span class="caret"></span></a>
        <div class="dropdown-menu">
            <a class="dropdown-item" href="#">Controllers: ${grailsApplication.controllerClasses.size()}</a>
            <a class="dropdown-item" href="#">Domains: ${grailsApplication.domainClasses.size()}</a>
            <a class="dropdown-item" href="#">Services: ${grailsApplication.serviceClasses.size()}</a>
            <a class="dropdown-item" href="#">Tag Libraries: ${grailsApplication.tagLibClasses.size()}</a>
        </div>
    </li>
    <li class="nav-item dropdown mr-4">
        <a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
           aria-expanded="false">Installed Plugins <span class="caret"></span></a>
        <div class="dropdown-menu">
            <g:each var="plugin" in="${applicationContext.getBean('pluginManager').allPlugins}">
                <a class="dropdown-item" href="#">${plugin.name} - ${plugin.version}</a>
            </g:each>
        </div>
    </li>
</content>

<div id="content" role="main" class="container">
    <section class="row">
        <h1>Welcome to Grails</h1>

        <p>
            Congratulations, you have successfully started your first Grails application! At the moment
            this is the default page, feel free to modify it to either redirect to a controller or display
            whatever content you may choose. Below is a list of controllers that are currently deployed in
            this application, click on each to execute its default action:
        </p>

        <div id="controllers" role="navigation">
            <h2>Available Controllers:</h2>
            <ul>
                <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                    <li class="controller">
                        <g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link>
                    </li>
                </g:each>
            </ul>
        </div>
    </section>
</div>

</body>
</html>
