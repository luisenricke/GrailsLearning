import ch.qos.logback.core.util.FileSize
import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
/*
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "logs/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
*/

// root(ERROR, ['STDOUT'])

jmxConfigurator('GrailsLearning')
jmxConfigurator('GrailsLearning:type=ch.qos.logback.classic.jmx.JMXConfigurator')

def timeOutput = timestamp("yyyy-MM-dd HH:mm:ss.SSS")
def timeFile = timestamp('yyyy-MM')
def utf8 = Charset.forName('UTF-8')
def appends = []
def logsFolder = ''

if (Environment.isDevelopmentMode()) {
    statusListener(OnConsoleStatusListener)
    logsFolder = 'logs'
    appends.add('CONSOLE')
    appends.add('FILES')
} else {
    logsFolder = '/opt/grailslearning/logs'
    appends.add('FILES_ROLLING')
}

println appends

if (appends.contains('CONSOLE')) {
    println 'console'
    appender('CONSOLE', ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            charset = utf8
            pattern = "%clr(%d{${timeOutput}}){magenta} " + // Date
                    '[%clr(%p)] ' + // Log level
                    '%clr(%logger){cyan}: ' + // Logger
                    "\n\t%m%n%wex" // Message
        }
    }
}

if (appends.contains('FILES')) {
    println 'files'
    appender("FILES", RollingFileAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{${timeOutput}} [%thread] %-5level %logger{40} - %msg%n"
        }

        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = "${logsFolder}/log-%d{yyyy-MM-dd}.log"
            maxHistory = 30
            totalSizeCap = FileSize.valueOf('100MB')
        }
    }
}

/*
if (appends.contains('FILES_ROLLING')) {
    appender("FILES_ROLLING", FileAppender) {

        encoder(PatternLayoutEncoder) {
            pattern = "%d{${timeOutput}} [%thread] %-5level %logger{40} - %msg%n"
        }
        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${logsFolder}/logs-%d${timeFile}}.%i.log"
            maxFileSize = '100mb'//FileSize.valueOf('100mb')
            maxHistory = 12
            totalSizeCap = '2gb'//FileSize.valueOf('2gb')
        }
    }
}
*/

logger('grailslearning', DEBUG, appends)

// https://docs.grails.org/latest/guide/conf.html
// http://logback.qos.ch/manual/groovy.html
// https://dennis-xlc.gitbooks.io/the-logback-manual/content/en/chapter-4-appenders/logback-core/rollingfileappender.html

// https://tridion.stackexchange.com/questions/253/logback-xml-limit-the-size-of-files
// https://lankydan.dev/2019/01/09/configuring-logback-with-spring-boot