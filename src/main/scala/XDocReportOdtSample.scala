import java.io._
import java.util._

import fr.opensagres.xdocreport.core.XDocReportException
import fr.opensagres.xdocreport.document.IXDocReport
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry
import fr.opensagres.xdocreport.template.IContext
import fr.opensagres.xdocreport.template.TemplateEngineKind
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata

object XDocReportOdtSample {
	def main(args: Array[String]) {
		// prepare tabular data to populate report -- not using model classes
		val listOfPersons = new ArrayList[HashMap[String, String]]()
		val person1 = new HashMap[String, String]()
		val person2 = new HashMap[String, String]()
		person1.put("name","John")
		person1.put("surname","Smith")
		listOfPersons.add(person1)
		person2.put("name","Homer")
		person2.put("surname","Simpson")
		listOfPersons.add(person2)

		// load report template
		val in: InputStream = getClass.getResourceAsStream("template.odt")

		// create report instance
		val report: IXDocReport = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity)

		// add metadata to specify the list-nature of the report placeholders
		val metadata = new FieldsMetadata()
		metadata.addFieldAsList("persons.name")
		metadata.addFieldAsList("persons.surname")
		report.setFieldsMetadata(metadata)

		// set data into report context
		val context: IContext = report.createContext()
		context.put("title", "XDocReport - ODT Template in Scala")
		context.put("persons", listOfPersons)

		// prepare output file stream
		val out: OutputStream = new FileOutputStream(new File("target/report.odt"))

		// process the report and output to file
		report.process(context, out)
	}
}
