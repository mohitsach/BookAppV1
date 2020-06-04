package com.mohit.BookAppV2.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mohit.BookAppV2.*
import com.mohit.BookAppV2.fragment.About
import com.mohit.BookAppV2.fragment.Dashboardfragment
import com.mohit.BookAppV2.fragment.FavouritesFragment
import com.mohit.BookAppV2.fragment.Profile

class MenuActivity : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorlayout:CoordinatorLayout
    lateinit var framelayout:FrameLayout
    lateinit var navigationview:NavigationView
    var previousmenuitem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        drawerLayout=findViewById(R.id.drawerlayout)
        coordinatorlayout=findViewById(R.id.coordinatorlayout)
        framelayout=findViewById(R.id.framelayout)
        navigationview=findViewById(R.id.navigationview)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Menu"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        opendashboard()
        val actionbardrawertoggle = ActionBarDrawerToggle(
            this@MenuActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionbardrawertoggle)
        actionbardrawertoggle.syncState()

        navigationview.setNavigationItemSelectedListener {
            if(previousmenuitem!=null){
                previousmenuitem?.isChecked=false
            }
             it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it


            when(it.itemId){
                R.id.Dashboard -> {
                    opendashboard()
                }
                R.id.Favourites ->{

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            FavouritesFragment()
                        )
                        .commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.Profile ->{

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            Profile()
                        )
                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.About ->{

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            About()
                        )
                        .commit()
                    supportActionBar?.title="About"
                    drawerLayout.closeDrawers()
                }
            }

            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }
    fun opendashboard(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout,
                Dashboardfragment()
            )
            .commit()
        supportActionBar?.title="Dashboard"
        drawerLayout.closeDrawers()
        navigationview.setCheckedItem(R.id.Dashboard)
    }

    override fun onBackPressed() {
       val frag=supportFragmentManager.findFragmentById(R.id.framelayout)
        when(frag){
            !is Dashboardfragment ->opendashboard()
            else -> super.onBackPressed()

        }
    }
}
