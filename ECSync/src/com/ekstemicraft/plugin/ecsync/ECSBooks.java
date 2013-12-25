
package com.ekstemicraft.plugin.ecsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class ECSBooks
{
    /* Title and Author strings */
    private String title, author;

    /* ArrayList of all page contents */
    private List<String> pages;

    public ECSBooks(String title, String author, List<String> pages)
    {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public static ArrayList<ECSBooks> loadBooks() throws FileNotFoundException
    {
        ArrayList<ECSBooks> books = new ArrayList<ECSBooks>();
        File[] bookSaves = ECSync.saveDir.listFiles();

        for (File bookSave : bookSaves)
        {
            Scanner scanner = new Scanner(bookSave);

            // Getting book title
            String title = bookSave.getName().split(".yml")[0];

            // Getting book author
            String author = scanner.nextLine().split(":")[1].trim();

            // Getting pages
            List<String> pages = new ArrayList<String>();

            String page = "";
            while (scanner.hasNext())
            {
                String line = scanner.nextLine();

                if (line.contains(">>page"))
                {
                    pages.add(page);
                    page = "";
                }
                else
                {
                    page += line + "\n";
                }
            }

            scanner.close();
            books.add(new ECSBooks(title, author, pages));
        }
        return books;
    }

    public static void saveBook(String bookName, BookMeta data)
    {
        try
        {
            PrintWriter writer =
                    new PrintWriter(new File(ECSync.saveDir, bookName
                            + ".yml"));

            writer.println("author:" + data.getAuthor());

            for (int i = 1; i <= data.getPageCount(); i++)
            {
                writer.println(data.getPage(i));
                writer.println(">>page " + i + "");

            }

            writer.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static ECSBooks getBook(String name)
    {
        for (ECSBooks book : ECSync.books)
        {
            if (book.getTitle().equalsIgnoreCase(name))
                return book;
        }
        return null;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public List<String> getPages()
    {
        return pages;
    }

    public void setPages(List<String> pages)
    {
        this.pages = pages;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public static void bookList(Player player)
    {
        String bookList = "";
        for (ECSBooks book : ECSync.books)
        {
            
                bookList += book.getTitle() + ", ";
            
        }

        // Getting rid of the last comma.
        if (!bookList.isEmpty())
            bookList = bookList.substring(0, bookList.length() - 2);

        player.sendMessage(ChatColor.AQUA
                + "Bookshelf:");
        player.sendMessage(ChatColor.GOLD + bookList);
    }

    public void spawnBook(Player player, int newguy)
    {
        
            int slot = player.getInventory().firstEmpty();
            if (slot != -1)
            {
                ItemFactory factory = Bukkit.getItemFactory();
                BookMeta meta =
                        (BookMeta) factory.getItemMeta(Material.WRITTEN_BOOK);

                meta.setAuthor(getAuthor());
                meta.setTitle(getTitle());
                meta.setPages(getPages());

                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                item.setItemMeta(meta);

                for (ItemStack i : player.getInventory().getContents())
                {
                    if (i == null)
                        continue;

                    if (i.isSimilar(item))
                    {
                        player.sendMessage(ChatColor.RED
                                + "You already have this book in your inventory!");
                        return;
                    }
                }

                player.getInventory().setItem(slot, item);
                if(newguy == 0){
                player.sendMessage(ChatColor.GREEN + "You received the book: "
                        + ChatColor.GOLD + meta.getTitle());
                }

            }
            else
            {
                player.sendMessage("Your inventory is full!");
            }
       
    }
}
