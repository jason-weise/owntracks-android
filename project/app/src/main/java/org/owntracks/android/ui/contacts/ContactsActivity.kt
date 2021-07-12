package org.owntracks.android.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.owntracks.android.R
import org.owntracks.android.databinding.UiContactsBinding
import org.owntracks.android.model.FusedContact
import org.owntracks.android.ui.base.BaseActivity
import org.owntracks.android.ui.base.BaseRecyclerViewAdapterWithClickHandler
import org.owntracks.android.ui.base.ClickHasBeenHandled
import org.owntracks.android.ui.map.MapActivity

@AndroidEntryPoint
class ContactsActivity : BaseActivity<UiContactsBinding?, ContactsMvvm.ViewModel<*>?>(),
        ContactsMvvm.View, BaseRecyclerViewAdapterWithClickHandler.ClickListener<FusedContact> {

    private lateinit var contactsAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactsAdapter = ContactsAdapter(this)
        setHasEventBus(false)
        bindAndAttachContentView(R.layout.ui_contacts, savedInstanceState)
        setSupportToolbar(binding!!.appbar.toolbar)
        setDrawer(binding!!.appbar.toolbar)
        binding!!.vm!!.contacts.observe({ this.lifecycle }, { contacts: Map<String, FusedContact> ->
            contactsAdapter.setData(contacts.values)
            binding!!.vm!!.refreshGeocodes()
        })
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.recyclerView.adapter = contactsAdapter
    }

    override fun onClick(
            `object`: FusedContact,
            view: View,
            longClick: Boolean
    ): ClickHasBeenHandled {
        val bundle = Bundle()
        bundle.putString(MapActivity.BUNDLE_KEY_CONTACT_ID, `object`.id)
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra("_args", bundle)
        startActivity(intent)
        return true
    }
}