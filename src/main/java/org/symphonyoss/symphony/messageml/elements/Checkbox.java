package org.symphonyoss.symphony.messageml.elements;

import org.commonmark.node.Node;
import org.symphonyoss.symphony.messageml.exceptions.InvalidInputException;
import org.symphonyoss.symphony.messageml.markdown.nodes.form.CheckboxNode;
import org.symphonyoss.symphony.messageml.util.XmlPrintStream;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class representing a Checkbox inside a Form.
 * @author Cristiano Faustino
 * @since 05/29/2019
 */
public class Checkbox extends FormElement {
  public static final String MESSAGEML_TAG = "checkbox";
  private static final String VALUE_ATTR = "value";
  private static final String CHECKED_ATTR = "checked";

  private static final String PRESENTATIONML_DIV_TAG = "div";
  private static final String PRESENTATIONML_CLASS_ATTR = "class";
  private static final String PRESENTATIONML_DIV_CLASS = "checkbox-group";

  private static final String PRESENTATIONML_INPUT_TAG = "input";
  private static final String PRESENTATIONML_TYPE_ATTR = "type";
  private static final String PRESENTATIONML_INPUT_TYPE = "checkbox";
  private static final String PRESENTATIONML_DEFAULT_CHECKBOX_VALUE = "on";

  private static final String PRESENTATIONML_LABEL_TAG = "label";

  public Checkbox(Element parent) {
    super(parent, MESSAGEML_TAG);
  }

  @Override
  public void validate() throws InvalidInputException {
    super.validate();

    if (getAttribute(NAME_ATTR) == null) {
      throw new InvalidInputException("The attribute \"name\" is required");
    }

    if (getAttribute(CHECKED_ATTR) != null) {
      assertAttributeValue(CHECKED_ATTR, Arrays.asList(Boolean.TRUE.toString(), Boolean.FALSE.toString()));
    }

    assertContentModel(Arrays.asList(TextNode.class, Bold.class, Italic.class));
    assertContainsChildOfType(Arrays.asList(TextNode.class, Bold.class, Italic.class));
  }

  @Override
  protected void buildAttribute(org.w3c.dom.Node item) throws InvalidInputException {
    switch (item.getNodeName()) {
      case NAME_ATTR:
        setAttribute(NAME_ATTR, getStringAttribute(item));
        break;
      case VALUE_ATTR:
        setAttribute(VALUE_ATTR, getStringAttribute(item));
        break;
      case CHECKED_ATTR:
        setAttribute(CHECKED_ATTR, getStringAttribute(item));
        break;
      default:
        throw new InvalidInputException("Attribute \"" + item.getNodeName()
            + "\" is not allowed in \"" + getMessageMLTag() + "\"");
    }
  }

  @Override
  public void asPresentationML(XmlPrintStream out) {
    out.openElement(PRESENTATIONML_DIV_TAG, PRESENTATIONML_CLASS_ATTR, PRESENTATIONML_DIV_CLASS);

    Map<String, String> presentationAttrs = buildCheckboxInputAttributes();
    out.printElement(PRESENTATIONML_INPUT_TAG, presentationAttrs);

    out.openElement(PRESENTATIONML_LABEL_TAG);
    for (Element child : getChildren()) {
      child.asPresentationML(out);
    }
    out.closeElement(); // Closing label

    out.closeElement(); // Closing div
  }

  @Override
  public Node asMarkdown() {
    return new CheckboxNode();
  }

  private Map<String, String> buildCheckboxInputAttributes() {
    Map<String, String> presentationAttrs = new LinkedHashMap<>();
    presentationAttrs.put(PRESENTATIONML_TYPE_ATTR, PRESENTATIONML_INPUT_TYPE);
    presentationAttrs.put(NAME_ATTR, getAttribute(NAME_ATTR));

    if (getAttribute(CHECKED_ATTR) != null) {
      presentationAttrs.put(CHECKED_ATTR, getAttribute(CHECKED_ATTR));
    }

    if (getAttribute(VALUE_ATTR) != null) {
      presentationAttrs.put(VALUE_ATTR, getAttribute(VALUE_ATTR));
    } else {
      presentationAttrs.put(VALUE_ATTR, PRESENTATIONML_DEFAULT_CHECKBOX_VALUE);
    }
    return presentationAttrs;
  }
}
