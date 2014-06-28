package io.vertigo.publisher.impl.merger.processor;

import io.vertigo.kernel.lang.Assertion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Classe d'utilitaires pour les fichiers de type Zip.
 *
 * @author pforhan
 * @version $Id: ZipUtil.java,v 1.4 2014/02/27 10:32:26 pchretien Exp $
 */
public final class ZipUtil {

	/**
	 * Encoder UTF8.
	 */
	private static final String ENCODER = "UTF-8";

	/**
	 * Taille du Buffer.
	 */
	private static final int BUFFER_SIZE = 8 * 1024;

	/**
	 * Constructeur priv� pour classe utilitaire
	 */
	private ZipUtil() {
		super();
	}

	/**
	 * Lecture d'un fichier du fichier ODT.
	 *
	 * @param odtFile ZipFile Fichier source
	 * @param entryName Nom de l'entr�e � extraire
	 * @return String le contenu du fichier sous forme de chaine encod�e avec ENCODER
	 * @throws IOException Si une exception d'entr�e-sortie de fichier a lieu
	 */
	public static String readEntry(final ZipFile odtFile, final String entryName) throws IOException {
		Assertion.checkNotNull(odtFile);
		Assertion.checkArgNotEmpty(entryName);
		final ZipEntry zipEntry = odtFile.getEntry(entryName);
		Assertion.checkNotNull(zipEntry, "Le mod�le {0} ne contient pas {1}, v�rifier que le mod�le est un document valide et du bon type.", odtFile.getName(), entryName);
		//---------------------------------------------------------------------
		final StringBuilder resultat = new StringBuilder();

		try (final InputStreamReader reader = new InputStreamReader(odtFile.getInputStream(zipEntry), ENCODER)) {
			final char[] buffer = new char[BUFFER_SIZE];
			int len;
			while ((len = reader.read(buffer, 0, BUFFER_SIZE)) > 0) {
				resultat.append(buffer, 0, len);
			}
		}
		return resultat.toString();
	}

	/**
	 * Ecriture d'une entry dans le fichier Zip � partir de son contenu et de son nom sous formes de chaine.
	 * .
	 * @param outputZipFile ZipOutputStream Fichier � modifier
	 * @param entryContent Contenu de l'entry � ins�rer
	 * @param entryName Nom de l'entry
	 * @throws IOException Si une exception d'entr�e sortie a lieu
	 */
	public static void writeEntry(final ZipOutputStream outputZipFile, final String entryContent, final String entryName) throws IOException {
		final ZipEntry content = new ZipEntry(entryName);
		outputZipFile.putNextEntry(content);
		final OutputStreamWriter writer = new OutputStreamWriter(outputZipFile, ENCODER);
		writer.write(entryContent, 0, entryContent.length());
		writer.flush();
	}

	/**
	 * Ecriture d'une entry dans le fichier Zip � partir de son contenu et de son nom sous formes de chaine.
	 * .
	 * @param outputZipFile ZipOutputStream Fichier � modifier
	 * @param entryContent Flux de l'entry � ins�rer
	 * @param entryName Nom de l'entry
	 * @throws IOException Si une exception d'entr�e sortie a lieu
	 */
	public static void writeEntry(final ZipOutputStream outputZipFile, final InputStream entryContent, final String entryName) throws IOException {
		writeEntry(outputZipFile, entryContent, new ZipEntry(entryName));
	}

	/**
	 * Ecriture d'une entry dans le fichier Zip � partir de son contenu et de son nom sous formes de chaine.
	 * .
	 * @param outputOdtFile ZipOutputStream Fichier � modifier
	 * @param entryContent Flux de l'entry � ins�rer
	 * @param zipEntry ZipEntry
	 * @throws IOException Si une exception d'entr�e sortie a lieu
	 */
	public static void writeEntry(final ZipOutputStream outputOdtFile, final InputStream entryContent, final ZipEntry zipEntry) throws IOException {
		outputOdtFile.putNextEntry(zipEntry);

		final int bufferSize = 10 * 1024;
		final byte[] bytes = new byte[bufferSize];
		int read;
		while ((read = entryContent.read(bytes)) > 0) {
			outputOdtFile.write(bytes, 0, read);
		}
		outputOdtFile.flush();
	}
}
