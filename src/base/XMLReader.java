package base;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import attacks.ActivityStarter;
import attacks.Broadcaster;
import brut.androlib.ApktoolProperties;
import components.Manifest;

/**
 * Marshal and Unmarshal Manifest file
 * @author aabolhadid
 *
 */
public class XMLReader{
	private String manifestFile;
	private Manifest manifest;
	
	public XMLReader(String manifestFile) {
		this.manifestFile = manifestFile;
		this.unmarshalManifest();
	}

	public void unmarshalManifest() {
		Manifest man=new Manifest();
		File manifest = new File(this.manifestFile);
		
		try {
	        
			JAXBContext context = JAXBContext.newInstance(Manifest.class);
			Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			man = (Manifest) jaxbUnmarshaller.unmarshal(manifest);
		}
		catch(Exception e)
		{
			System.out.println("ERROR: Manifest cannot be parsed");
			e.printStackTrace();
		}
		
		this.manifest = man;
	}
	
	public Manifest getManifest() {
		return this.manifest;
		
	}

}
