package edu.umn.msi.tropix.common.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Applys default namespace to XML InputSource.
 * 
 * See http://stackoverflow.com/questions/277502/jaxb-how-to-ignore-namespace-during-unmarshalling-xml-document
 * 
 * @author John Chilton (jmchilton at gmail dot com)
 * 
 */
class NamespaceFilter extends XMLFilterImpl {

  private String usedNamespaceUri;
  private final boolean addNamespace;

  // State variable
  private boolean addedNamespace = false;

  public NamespaceFilter(final String namespaceUri, final boolean addNamespace) {
    super();

    if(addNamespace) {
      this.usedNamespaceUri = namespaceUri;
    } else {
      this.usedNamespaceUri = "";
    }
    this.addNamespace = addNamespace;
  }

  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
    if(addNamespace) {
      startControlledPrefixMapping();
    }
  }

  @Override
  public void startElement(final String arg0, final String arg1, final String arg2, final Attributes arg3) throws SAXException {

    super.startElement(this.usedNamespaceUri, arg1, arg2, arg3);
  }

  @Override
  public void endElement(final String arg0, final String arg1, final String arg2) throws SAXException {
    super.endElement(this.usedNamespaceUri, arg1, arg2);
  }

  @Override
  public void startPrefixMapping(final String prefix, final String url) throws SAXException {
    if(addNamespace) {
      this.startControlledPrefixMapping();
    } 
    //else {
    // Remove the namespace, i.e. don´t call startPrefixMapping for parent!
    //}
  }

  private void startControlledPrefixMapping() throws SAXException {

    if(this.addNamespace && !this.addedNamespace) {
      // We should add namespace since it is set and has not yet been done.
      super.startPrefixMapping("", this.usedNamespaceUri);

      // Make sure we dont do it twice
      this.addedNamespace = true;
    }
  }

}
