package es.ctic.parrot.docmodel;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.ctic.parrot.reader.Input;

/**
 * 
 * A document is a group of detailed views that will be used by a generator.
 * @author <a href="http://www.fundacionctic.org">CTIC Foundation</a>
 * @version 1.0
 * @since 1.0
 *
 */
public class Document {
	private String title;
	private final Collection<Input> inputs = new HashSet<Input>();
	private final Set<OntologyDetailView> ontologyDetailViews = new HashSet<OntologyDetailView>();
	private final Set<OntologyClassDetailView> ontologyClassDetailViews = new HashSet<OntologyClassDetailView>();
	private final Set<OntologyPropertyDetailView> ontologyPropertyDetailViews = new HashSet<OntologyPropertyDetailView>();
    private final Set<RuleDetailView> ruleDetailViews = new HashSet<RuleDetailView>();
    private final Set<RuleSetDetailView> ruleSetDetailViews = new HashSet<RuleSetDetailView>();
    private final Set<OntologyIndividualDetailView> ontologyIndividualDetailViews = new HashSet<OntologyIndividualDetailView>();
    private final Glossary glossary;
    private String prologueURL;
    private String appendixURL;

    /**
     * Constructs a document using the given locale.
     * @param locale the locale.
     */
    public Document(Locale locale) {
        this.glossary = new Glossary(locale);
    }
	
    /**
     * Returns the title of this document.
     * @return the title of this document.
     */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set the title.
	 * @param title the title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the sorted collection of ontology views for this document. Elements are sorted alphabetically by label.
	 * @return the sorted collection of ontology views for this document.
	 */
	public Collection<OntologyDetailView> getOntologyDetailViews() {
		List<OntologyDetailView> listOntologyDetailsViews = new LinkedList<OntologyDetailView>(this.ontologyDetailViews);
		Collections.sort(listOntologyDetailsViews, new DetailViewComparator());
		return listOntologyDetailsViews;
	}

	/**
	 * Returns the sorted collection of class views for this document. Elements are sorted alphabetically by label.
	 * @return the sorted collection of class views for this document.
	 */
	public Collection<OntologyClassDetailView> getOntologyClassDetailViews() {
		List<OntologyClassDetailView> listOntologyClassDetailViews = new LinkedList<OntologyClassDetailView>(this.ontologyClassDetailViews);
		Collections.sort(listOntologyClassDetailViews, new DetailViewComparator());
		return listOntologyClassDetailViews;
	}

	/**
	 * Returns the sorted collection of property views for this document. Elements are sorted alphabetically by label.
	 * @return the sorted collection of property views for this document.
	 */
	public Collection<OntologyPropertyDetailView> getOntologyPropertyDetailViews() {
		List<OntologyPropertyDetailView> listOntologyPropertyDetailViews = new LinkedList<OntologyPropertyDetailView>(this.ontologyPropertyDetailViews);
		Collections.sort(listOntologyPropertyDetailViews, new DetailViewComparator());
		return listOntologyPropertyDetailViews;
	}
	
	/**
	 * Returns the sorted collection of rule views for this document. Elements are sorted alphabetically by label.
	 * @return the sorted collection of rule views for this document.
	 */
    public Collection<RuleDetailView> getRuleDetailViews() {
		List<RuleDetailView> listRuleDetailViews = new LinkedList<RuleDetailView>(this.ruleDetailViews);
		Collections.sort(listRuleDetailViews, new DetailViewComparator());
		return listRuleDetailViews;
    }
    
	/**
	 * Returns the sorted collection of rule set views for this document. Elements are sorted alphabetically by label.
	 * @return the sorted collection of rule set views for this document.
	 */
    public Collection<RuleSetDetailView> getRuleSetDetailViews() {
		List<RuleSetDetailView> listRuleSetDetailViews = new LinkedList<RuleSetDetailView>(this.ruleSetDetailViews);
		Collections.sort(listRuleSetDetailViews, new DetailViewComparator());
		return listRuleSetDetailViews;
    }
    
	/**
	 * Returns the sorted collection of individual views for this document. Elements are sorted alphabetically by label.
	 * @return the sorted collection of individual views for this document.
	 */
    public Collection<OntologyIndividualDetailView> getOntologyIndividualDetailViews() {
		List<OntologyIndividualDetailView> listOntologyIndividualDetailViews = new LinkedList<OntologyIndividualDetailView>(this.ontologyIndividualDetailViews);
		Collections.sort(listOntologyIndividualDetailViews, new DetailViewComparator());
		return listOntologyIndividualDetailViews;
    }

    /** 
     * Adds a given detailed ontology view into this document.
     * @param details the detailed ontology view.
     */
    public void addOntologyDetailView(OntologyDetailView details) {
        this.ontologyDetailViews.add(details);
    }
    
    /** 
     * Adds a given detailed class view into this document.
     * @param details the detailed class view.
     */
    public void addOntologyClassDetailView(OntologyClassDetailView details) {
        this.ontologyClassDetailViews.add(details);
    }
    
    /** 
     * Adds a given detailed property view into this document.
     * @param details the detailed property view.
     */
    public void addOntologyPropertyDetailView(OntologyPropertyDetailView details) {
        this.ontologyPropertyDetailViews.add(details);
    }
    
    /** 
     * Adds a given detailed individual view into this document.
     * @param details the detailed individual view.
     */
	public void addOntologyIndividualDetailView(OntologyIndividualDetailView details) {
		this.ontologyIndividualDetailViews.add(details);
	}
	
    /** 
     * Adds a given detailed rule view into this document.
     * @param details the detailed rule view.
     */
    public void addRuleDetailView(RuleDetailView details) {
        this.ruleDetailViews.add(details);
    }
    
    /** 
     * Adds a given detailed rule set view into this document.
     * @param details the detailed rule set view.
     */
    public void addRuleSetDetailView(RuleSetDetailView details) {
        this.ruleSetDetailViews.add(details);
    }

	/**
	 * Returns the glossary.
	 * @return the glossary.
	 */
    public Glossary getGlossary() {
        return glossary;
    }

	/**
	 * @return the inputs
	 */
	public Collection<Input> getInputs() {
		return inputs;
	}
	
	public void setInputs(Collection<Input> inputs){
		this.inputs.addAll(inputs);
	}

	/**
	 * @return the prologue
	 */
	public String getPrologueURL() {
		return prologueURL;
	}

	/**
	 * @param prologue the prologue to set
	 */
	public void setPrologueURL(String prologueURL) {
		this.prologueURL = prologueURL;
	}
	
	public String getPrologue(){
		return getContentFromURL(getPrologueURL());
	}
	
	/**
	 * @param appendixURL the appendixURL to set
	 */
	public void setAppendixURL(String appendixURL) {
		this.appendixURL = appendixURL;
	}

	/**
	 * @return the appendixURL
	 */
	public String getAppendixURL() {
		return appendixURL;
	}
	
	public String getAppendix(){
		return getContentFromURL(getAppendixURL());
	}
	
	private String getContentFromURL(String URL){
		
	    try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);

			DocumentBuilder builder = domFactory.newDocumentBuilder();
			
			builder.setEntityResolver(new EntityResolver() {
			    public InputSource resolveEntity(String publicId, String systemId)
			            throws SAXException, IOException {
			            return new InputSource(new StringReader(""));
			    }
			});

			String anchor = new URL(URL).getRef();

			org.w3c.dom.Document doc = builder.parse(URL);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//*[@id='"+anchor+"']");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			StringWriter buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(nodes.item(0)), new StreamResult(buffer));
			return buffer.toString();
		} catch (Exception e){
			return null; //FIXME ashame code
		}
	}
}
/**
 * Compares alphabetically by label of each <code>DetailView</code> 
 *
 */
class DetailViewComparator implements Comparator<DetailView> {

	// This comparator is not consistent with equals()
	public int compare(DetailView arg0, DetailView arg1) {
		if (arg0.getLabel() != null && arg1.getLabel() != null) {
			return  arg0.getLabel().toLowerCase().compareTo(arg1.getLabel().toLowerCase());
		} else {
			return 0; // FIXME change comparable method
		}
	}
	
}

