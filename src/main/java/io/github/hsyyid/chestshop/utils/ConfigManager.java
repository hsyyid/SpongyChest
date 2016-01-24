package io.github.hsyyid.chestshop.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hsyyid.chestshop.SpongyChest;
import org.spongepowered.api.world.Location;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ConfigManager
{
	private static Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).create();

	public static void readChestShops()
	{
		String json = null;

		try
		{
			json = readFile("ChestShops.json", StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			System.out.println("Could not read JSON file!");
		}

		if (json != null)
		{
			SpongyChest.chestShops = new ArrayList<ChestShop>(Arrays.asList(gson.fromJson(json, ChestShop[].class)));
		}
		else
		{
			System.out.println("No JSON data read.");
		}
	}

	public static void writeChestShops()
	{
		String json = gson.toJson(SpongyChest.chestShops);

		try
		{
			// Assume default encoding.
			FileWriter fileWriter = new FileWriter("ChestShops.json");

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			bufferedWriter.write(json);

			bufferedWriter.flush();
			// Always close files.
			bufferedWriter.close();
		}
		catch (IOException ex)
		{
			System.out.println("Could not save JSON file!");
		}
	}

	static String readFile(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
