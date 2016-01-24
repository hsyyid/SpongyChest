package io.github.hsyyid.chestshop.utils;

import io.github.hsyyid.chestshop.SpongyChest;

import java.io.IOException;
import java.util.UUID;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class LocationAdapter extends TypeAdapter<Location<World>>
{
	@Override
	public void write(JsonWriter out, Location<World> location) throws IOException
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
	public Location<World> read(JsonReader in) throws IOException
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

		if (SpongyChest.game.getServer().getWorld(UUID.fromString(worldID)).isPresent())
		{
			Location<World> location = new Location<World>(SpongyChest.game.getServer().getWorld(UUID.fromString(worldID)).get(), x, y, z);
			return location;
		}
		else
		{
			return null;
		}
	}

}