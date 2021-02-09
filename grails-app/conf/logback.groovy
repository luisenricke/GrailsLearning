import ch.qos.logback.core.util.FileSize
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

jmxConfigurator('GrailsLearning')
jmxConfigurator('GrailsLearning:type=ch.qos.logback.classic.jmx.JMXConfigurator')

def timeOutput = timestamp("yyyy-MM-dd HH:mm:ss.SSS")
def timeFile = 'yyyy-MM-dd'
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

if (appends.contains('CONSOLE')) {
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
    appender("FILES", RollingFileAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%-5level %d{${timeOutput}} [%thread] %logger{40} - %msg%n"
        }

        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "$logsFolder/log-%d{${timeFile}}.%i.log"
            maxFileSize = FileSize.valueOf('10MB')
            maxHistory = 12
            totalSizeCap = FileSize.valueOf('100MB')
        }
    }
}

if (appends.contains('FILES_ROLLING')) {
    appender("FILES_ROLLING", RollingFileAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{${timeOutput}} [%thread] %-5level %logger{40} - %msg%n"
        }

        rollingPolicy(SizeAndTimeBasedRollingPolicy) {
            fileNamePattern = "${logsFolder}/log-%d{${timeFile}}.%i.log"
            maxFileSize = FileSize.valueOf('100MB')
            maxHistory = 30
            totalSizeCap = FileSize.valueOf('1GB')
        }
    }
}


root(INFO, appends)
// logger('grailslearning', DEBUG, appends)

// https://docs.grails.org/latest/guide/conf.html
// http://logback.qos.ch/manual/groovy.html
// https://logback.qos.ch/manual/appenders.html
// https://dennis-xlc.gitbooks.io/the-logback-manual/content/en/chapter-4-appenders/logback-core/rollingfileappender.html

// https://tridion.stackexchange.com/questions/253/logback-xml-limit-the-size-of-files
// https://lankydan.dev/2019/01/09/configuring-logback-with-spring-boot