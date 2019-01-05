package com.kirelcodes.milker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;
import com.kirelcodes.miniaturepets.MiniaturePets;
import com.kirelcodes.miniaturepets.mob.MobContainer;
import com.kirelcodes.miniaturepets.utils.APIUtils;

public class Constants
{
	private static MobContainer _milkManContainer = null;

	public static boolean init()
	{
		try 
		{
			_milkManContainer = APIUtils.loadContainer(getFile("Milker.mpet", Main.getInstance()));
		}
		catch(IOException e)
		{
			if(MiniaturePets.isDebug())
				e.printStackTrace();
			return false;
		}
		return true;
	}

	private static File getFile(String name, JavaPlugin plugin) throws IOException
	{
		File tempDir = Files.createTempDir();
		File tempFile = new File(tempDir, name);
		FileOutputStream out = new FileOutputStream(tempFile);
		IOUtils.copy(plugin.getResource(name), out);
		return tempFile;
	}

	public static MobContainer getMilkerContainer()
	{
		return _milkManContainer;
	}

}
