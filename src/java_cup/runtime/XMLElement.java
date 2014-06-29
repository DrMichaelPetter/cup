package java_cup.runtime;

import java.util.Arrays;
import java.util.LinkedList;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java_cup.runtime.ComplexSymbolFactory.Location;

public abstract class XMLElement {
	public static void dump(XMLStreamWriter writer, XMLElement elem) throws XMLStreamException {
		writer.writeStartDocument();
		elem.dump(writer);
		writer.writeEndDocument();
	}
	protected String tagname;
	public abstract Location right();
	public abstract Location left();
	protected abstract void dump(XMLStreamWriter writer) throws XMLStreamException;

	public static class NonTerminal extends XMLElement {
		private int variant;
		LinkedList<XMLElement> list;
		public NonTerminal(String tagname, int variant, XMLElement... l) {
			this.tagname=tagname;
			this.variant=variant;
			list = new LinkedList<XMLElement>(Arrays.asList(l));
		}

		public Location left() {	return list.getFirst().left();	}
		public Location right() {	return list.getLast().right();	}

		public String toString() {
			String ret = "<nonterminal id=\"" + tagname +"\" left=\"" + left()
					+ "\" right=\"" + right() + "\" variant=\""+variant+"\">";
			for (XMLElement e : list)
				ret += e.toString();
			return ret + "</nonterminal>";
		}
		@Override
		protected void dump(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeStartElement("nonterminal");
			writer.writeAttribute("id", tagname);
			writer.writeAttribute("variant", variant+"");
			writer.writeAttribute("left", left()+"");
			writer.writeAttribute("right", right()+"");
			writer.writeEndElement();
		}
	}

	public static class Error extends XMLElement {
		Location l,r;
		public Error(Location l, Location r) {
			this.l=l;
			this.r=r;
		}
		public Location left() {	return l; 	}
		public Location right() {	return r;	}

		public String toString() {
			return  "<error left=\"" + l + "\" right=\"" + r + "\"/>";
		}
		@Override
		protected void dump(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeStartElement("error");
			writer.writeAttribute("left", left()+"");
			writer.writeAttribute("right", right()+"");
			writer.writeEndElement();
		}
	}
	
	public static class Terminal extends XMLElement {
		Location l, r;
		Object value;

		public Terminal(Location l, String symbolname, Location r) {
			this(l, symbolname, null, r);
		}

		public Terminal(Location l, String symbolname, Object i, Location r) {
			this.l = l;
			this.r = r;
			this.value = i;
			this.tagname = symbolname;
		}

		public Location left() {	return l; 	}
		public Location right() {	return r;	}

		public String toString() {
			return (value == null) ? "<terminal id=\"" + tagname + "\"/>"
					: "<terminal id=\"" + tagname + "\" left=\"" + l
							+ "\" right=\"" + r + "\">" + value
							+ "</terminal>";
		}
		@Override
		protected void dump(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeStartElement("terminal");
			writer.writeAttribute("id", tagname);
			writer.writeAttribute("left", left()+"");
			writer.writeAttribute("right", right()+"");
			writer.writeEndElement();
		}
	}
}
