package es.ctic.parrot.reader.jena;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import es.ctic.parrot.de.DocumentableObjectRegister;
import es.ctic.parrot.reader.DocumentReader;
import es.ctic.parrot.reader.Input;
import es.ctic.parrot.reader.ReaderException;

public class JenaOWLReader implements DocumentReader {
    
    private static final Logger logger = Logger.getLogger(JenaOWLReader.class);
	
	/* (non-Javadoc)
	 * @see es.ctic.parrot.reader.DocumentReader#readDocumentableObjects(es.ctic.parrot.reader.Input, es.ctic.parrot.de.DocumentableObjectRegister)
	 */
	public void readDocumentableObjects(Input input, DocumentableObjectRegister register) throws IOException, ReaderException {
        OntModel model = ModelFactory.createOntologyModel();
        String base = null;
        
        try {
            logger.debug("Parsing OWL file");
            model.read(input.openReader(), base, getJenaFormat(input));
            loadOntology(model, register);
            loadOntClasses(model, register);
            loadOntProperties(model, register);
            loadOntIndividuals(model, register);
        } catch (JenaException e) {
            if (e.getCause() != null && e.getCause() instanceof SAXParseException) {
                throw new ReaderException("OWL parse error: " + e.getCause().getMessage(), e);
            } else {
                throw new ReaderException("While reading OWL file", e);
            }
        }
	}

	private static String getJenaFormat(Input input) {
	    if ("application/rdf+xml".equals(input.getMimeType())) {
	        return "RDF/XML";
	    } else if ("text/turtle".equals(input.getMimeType())) {
	        return "TURTLE";
	    } else {
	        return "RDF/XML"; // default
	    }
    }

    private static void loadOntClasses(OntModel model, DocumentableObjectRegister register) {
		Iterator<OntClass> it= model.listNamedClasses();
		while(it.hasNext()){
			OntClass ontclass=it.next();
			if (isDomainSpecific(ontclass)) {
			    AbstractJenaDocumentableObject oce=new OntologyClassJenaImpl(ontclass);
			    register.registerDocumentableObject(oce);
			}
		}
	}
	
	private static void loadOntProperties(OntModel model, DocumentableObjectRegister register) {
	    Iterator<OntProperty> it = model.listAllOntProperties();
	    while (it.hasNext()) {
	        OntProperty ontProperty = it.next();
	        if (isDomainSpecific(ontProperty)) {
	            OntologyPropertyJenaImpl docObject = new OntologyPropertyJenaImpl(ontProperty);
	            register.registerDocumentableObject(docObject);	        
	        }
	    }
	}
	
	private static void loadOntology(OntModel model, DocumentableObjectRegister register) {
	    Iterator<Ontology> it = model.listOntologies();
	    while (it.hasNext()) {
	    	Ontology ontology = it.next();
            OntologyJenaImpl docObject = new OntologyJenaImpl(ontology);
            register.registerDocumentableObject(docObject);	        
	    }
	}

	private static void loadOntIndividuals(OntModel model, DocumentableObjectRegister register) {
	    Iterator<Individual> it = model.listIndividuals();
	    while (it.hasNext()) {
	    	Individual individual = it.next();
	    	if (isDomainSpecific(individual) && isClassDomainSpecific(individual)) {
	        	OntologyIndividualJenaImpl docObject = new OntologyIndividualJenaImpl(individual);
	        	register.registerDocumentableObject(docObject);
	        }
	    }
	}
	
	private static boolean isDomainSpecific(OntResource ontResource) {
        String uri = ontResource.getURI();
        return !uri.startsWith(RDFS.getURI()) && !uri.startsWith(RDF.getURI()) && !uri.startsWith(OWL.getURI());
    }

	private static boolean isClassDomainSpecific(Individual individual) {
    	for(OntClass ontClass : individual.listOntClasses(true).toList()){
   			if (isDomainSpecific(ontClass)){
   				return true;
    		}
    	}
        return false;
    }

}
