package io.github.hsyyid.chestshop.utils;

import io.github.hsyyid.chestshop.Main;

import java.io.IOException;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LocationAdapter extends TypeAdapter<Location>
{
	@Override
	public void write(JsonWriter out, Location location) throws IOException
	{
		if (location == null)
		{
			out.nullValue();
			return;
		}

		out.beginObject();

		if (location.getExtent() instanceof World)
		{
			out.name("world");
			out.value(((World) location.getExtent()).getUniqueId().toString());
		}
		out.name("x");
		out.value(location.getX());

		out.name("y");
		out.value(location.getY());

		out.name("z");
		out.value(location.getZ());

		out.endObject();

	}

	@Override
	public Location read(JsonReader in) throws IOException
	{
		if (in.peek() == JsonToken.NULL)
		{
			return null;
		}

		in.beginObject();

		in.nextName();
		String worldID = in.nextString();

		in.nextName();
		double x = in.nextDouble();

		in.nextName();
		double y = in.nextDouble();

		in.nextName();
		double z = in.nextDouble();

		in.endObject();

		if (Main.game.getServer().getWorld(UUID.fromString(worldID)).isPresent())
		{
			Location location = new Location(Main.game.getServer().getWorld(UUID.fromString(worldID)).get(), x, y, z);
			return location;
		}
		else
		{
			return null;
		}
	}

}